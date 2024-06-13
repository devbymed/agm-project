import { Permission } from "@features/admin/models/permission.model";

export interface Profile {
  id: number;
  name: string;
  permissions: Permission[];
}
