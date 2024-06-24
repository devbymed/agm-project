import { NgFor } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { Profile } from "@core/models/profile.model";
import { Permission } from "@features/admin/models/permission.model";
import { PermissionService } from "@features/admin/services/permission.service";
import { ProfileService } from "@features/admin/services/profile.service";
import { TreeNode } from "primeng/api";
import { TreeModule } from 'primeng/tree';

@Component({
  selector: 'app-authorizations',
  standalone: true,
  imports: [TreeModule, NgFor],
  templateUrl: './authorizations.component.html',
  styleUrls: ['./authorizations.component.css']
})
export class AuthorizationsComponent implements OnInit {
  profiles: Profile[] = [];
  treeData: { [key: string]: TreeNode[] } = {};
  selectedNodes: { [key: string]: TreeNode[] } = {};

  constructor(
    private profileService: ProfileService,
    private permissionService: PermissionService
  ) { }

  ngOnInit() {
    this.loadProfilesAndPermissions();
  }

  loadProfilesAndPermissions() {
    this.profileService.getProfiles().subscribe(profileResponse => {
      if (profileResponse.status === 'OK') {
        this.profiles = profileResponse.data || []
        this.permissionService.getPermissionTree().subscribe(permissionResponse => {
          if (permissionResponse.status === 'OK') {
            const allPermissions = permissionResponse.data;
            this.profiles.forEach(profile => {
              this.treeData[profile.name] = this.convertPermissionsToTreeNodes(allPermissions || [], profile.permissions);
              this.selectedNodes[profile.name] = this.getSelectedNodes(this.treeData[profile.name]);
              this.updateNodeSelectionState(this.treeData[profile.name], profile.name);
            });
          }
        });
      }
    });
  }

  convertPermissionsToTreeNodes(permissions: Permission[], selectedPermissions: Permission[]): TreeNode[] {
    return permissions.map(permission => {
      const isSelected = selectedPermissions.some(sp => sp.id === permission.id);
      const node: TreeNode = {
        key: permission.id?.toString() || '',
        label: permission.label,
        data: { ...permission, selected: isSelected },
        children: permission.children ? this.convertPermissionsToTreeNodes(permission.children, selectedPermissions) : [],
        selectable: true,
        partialSelected: false
      };
      return node;
    });
  }

  getSelectedNodes(treeNodes: TreeNode[]): TreeNode[] {
    let selectedNodes: TreeNode[] = [];
    treeNodes.forEach(node => {
      if (node.children && node.children.length) {
        const childSelectedNodes = this.getSelectedNodes(node.children);
        if (childSelectedNodes.length > 0) {
          selectedNodes = [...selectedNodes, ...childSelectedNodes];
          node.partialSelected = true;
        }
      }
      if (node.data.selected) {
        selectedNodes.push(node);
        node.partialSelected = false;
      }
    });
    return selectedNodes;
  }

  onPermissionChange(event: any, profileName: string): void {
    this.selectedNodes[profileName] = event;
    this.updateNodeSelectionState(this.treeData[profileName], profileName);
  }

  updateNodeSelectionState(treeNodes: TreeNode[], profileName: string): void {
    treeNodes.forEach(node => {
      if (node.children && node.children.length) {
        this.updateNodeSelectionState(node.children, profileName);
        const allChildrenSelected = node.children.every(child => this.selectedNodes[profileName]?.includes(child));
        const someChildrenSelected = node.children.some(child => this.selectedNodes[profileName]?.includes(child) || child.partialSelected);

        node.partialSelected = !allChildrenSelected && someChildrenSelected;
        if (allChildrenSelected && !this.selectedNodes[profileName]?.includes(node)) {
          this.selectedNodes[profileName]?.push(node);
        } else if (!allChildrenSelected && this.selectedNodes[profileName]?.includes(node)) {
          const index = this.selectedNodes[profileName]?.indexOf(node);
          this.selectedNodes[profileName]?.splice(index, 1);
        }
      }
    });
  }
}