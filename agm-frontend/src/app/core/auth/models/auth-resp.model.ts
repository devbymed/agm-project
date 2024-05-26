import { User } from '@core/models/user.model';

export interface AuthResp {
  accessToken: string;
  firstLogin: boolean;
  user: User;
}
