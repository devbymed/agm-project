export interface ApiResp<T> {
  status: string;
  message?: string;
  data?: T;
}
