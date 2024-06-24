import { Permission } from "@features/admin/models/permission.model";
import { TreeNode } from "primeng/api";

export interface Profile {
  id: number;
  name: string;
  permissions: Permission[];
  treePermissions?: TreeNode[];
}
