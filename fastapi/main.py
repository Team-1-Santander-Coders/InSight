from fastapi import FastAPI, HTTPException, Response
from typing import Optional, Dict, Any, Union
from pydantic import BaseModel
from collectors import notebook
from entities.data_request import SummaryDataRequest, GenerateAudioRequest
from entities.summarized_data import SummarizedData
from collectors.collect_data import collect_data
from factories.driver import setup_driver
from entities.types.operation_type import OperationType
from contextlib import asynccontextmanager
import uuid
import asyncio
from datetime import datetime


class ProcessingResult(BaseModel):
    status: str
    data: Optional[Union[SummarizedData, str]] = None
    error: Optional[str] = None


class QueueProcessor:
    def __init__(self):
        self.queue = asyncio.Queue()
        self.results: Dict[str, ProcessingResult] = {}
        self.driver = None

    async def initialize(self):
        self.driver = setup_driver()
        notebook.login_notebook_driver(driver=self.driver)

    async def process_queue(self):
        while True:
            try:
                query_id, operation_type, term, data = await self.queue.get()
                result = ProcessingResult(status="processing")

                try:
                    if data:
                        if operation_type == OperationType.SUMMARIZE:
                            processed_data = await notebook.get_summary(
                                driver=self.driver,
                                term=term,
                                data=data
                            )
                            result = ProcessingResult(
                                status="success",
                                data=processed_data
                            )
                        elif operation_type == OperationType.GENERATEAUDIO:
                            processed_data = await notebook.turn_audio_public(
                                driver=self.driver,
                                data=data
                            )
                            result = ProcessingResult(
                                status="success",
                                data=processed_data
                            )
                    else:
                        raise ValueError("No data provided for processing")

                except Exception as e:
                    print(e)
                    result = ProcessingResult(
                        status="error",
                        error=f"Error processing request: {str(e)}"
                    )

                self.results[query_id] = result
                self.queue.task_done()

            except Exception as e:
                print(f"Queue processing error: {str(e)}")
                await asyncio.sleep(1)


queue_processor = QueueProcessor()

@asynccontextmanager
async def lifespan(app: FastAPI):
    await queue_processor.initialize()
    asyncio.create_task(queue_processor.process_queue())
    yield
    if queue_processor.driver:
        queue_processor.driver.quit()

app = FastAPI(lifespan=lifespan)


@app.post("/summarize", response_model=SummarizedData)
async def get_summary(request_data: SummaryDataRequest) -> SummarizedData | ProcessingResult:
    """
    Process a summary request. Date should be in ISO8601 format (yyyy-mm-dd)
    """
    print(request_data)
    try:
        query_id = str(uuid.uuid4())
        collected_data = await collect_data(request_data)
        print(collect_data)

        await queue_processor.queue.put((
            query_id,
            OperationType.SUMMARIZE,
            request_data.term,
            collected_data
        ))

        await queue_processor.queue.join()
        result = queue_processor.results.pop(query_id, None)
        if result.status == "success":
            return result.data
        else:
            raise HTTPException(status_code=503, detail=result.error)

    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))
