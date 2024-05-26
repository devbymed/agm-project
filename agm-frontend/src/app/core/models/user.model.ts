import { Profile } from './profile.model';

export interface User {
  firstName: string;
  lastName: string;
  email: string;
  profile: Profile;
}
