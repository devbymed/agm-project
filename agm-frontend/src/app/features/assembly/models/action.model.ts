import { Document } from "@core/models/document.model";

export interface Action {
  id: number;
  description: string;
  responsible: string;
  startDate: string;
  endDate: string;
  progressStatus: string;
  realizationDate?: string;
  observation?: string;
  deliverable?: string;
  attachments: Document[];
}
