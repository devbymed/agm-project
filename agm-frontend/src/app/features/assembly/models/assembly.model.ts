import { Document } from "@core/models/document.model";
import { Action } from "./action.model";

export interface Assembly {
  id: number;
  type: string;
  year: number;
  day: string;
  time: string;
  address: string;
  city: string;
  closed: boolean;
  routeSheet: Document;
  invitationLetter: Document;
  attendanceSheet: Document;
  proxy: Document;
  attendanceForm: Document;
  actions: Action[];
}
