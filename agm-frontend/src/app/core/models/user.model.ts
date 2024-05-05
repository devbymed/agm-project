import { Profile } from './profile.model';

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  profile: Profile;
}
