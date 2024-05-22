import { User } from '@core/models/user.model';

export interface AuthResponse {
  accessToken: string;
  user: User;
  mustChangePassword: boolean;
}
