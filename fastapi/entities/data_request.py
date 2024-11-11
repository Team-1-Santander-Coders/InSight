from pydantic import BaseModel

class GenerateDataRequest(BaseModel):
    prompt: str

class SummaryDataRequest(BaseModel):
    term: str
    start_date: str
    end_date: str

class GenerateAudioRequest(BaseModel):
    summary_id: str