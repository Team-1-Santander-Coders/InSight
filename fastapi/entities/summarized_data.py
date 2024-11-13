from pydantic import BaseModel
from typing import List

class SummarizedData(BaseModel):
    id: str
    summary: str
    description: str
    categories: str
    image_url: str
    audio_url: str
    references: List[str]