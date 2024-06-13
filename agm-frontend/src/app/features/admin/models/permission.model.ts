export interface Permission {
  id: number;
  name: string;
  label: string;
  children: Permission[];
}
