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
  routeSheet: String;
  invitationLetter: String;
  attendanceSheet: String;
  proxy: String;
  attendanceForm: String;
  actions: Action[];
}
