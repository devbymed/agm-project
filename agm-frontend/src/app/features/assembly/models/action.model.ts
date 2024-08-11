import { ActionAttachment } from "./action-attachment.model";

export interface Action {
  id: number;
  name: string;
  entity: string;
  startDate?: string;
  endDate?: string;
  realizationDate?: string;
  responsible?: string;
  deliverable?: string;
  progressStatus: number;
  observation?: string;
  attachments?: ActionAttachment[]; // Ajout des pièces jointes
}
