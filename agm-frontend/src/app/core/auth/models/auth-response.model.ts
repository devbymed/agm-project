import { User } from '@core/models/user.model';

export interface AuthResponse {
  accessToken: string;
  firstLogin: boolean;
  user: User;
}
