export interface Permission {
  id: number;
  name: string;
  label: string;
  path: string;
  // parentId?: number;
  children?: Permission[];
}
