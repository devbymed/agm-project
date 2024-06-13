import { NgFor } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { ApiResponse } from "@core/models/api-response.model";
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
  permissions: TreeNode[] = [];
  selectedPermissions: { [key: number]: TreeNode[] } = {};

  constructor(
    private profileService: ProfileService,
    private permissionService: PermissionService
  ) { }

  ngOnInit(): void {
    this.loadProfiles();
    this.loadPermissions();
  }

  loadProfiles(): void {
    this.profileService.getProfiles().subscribe((response: ApiResponse<Profile[]>) => {
      if (response.status === 'OK') {
        this.profiles = response.data || [];
        this.profiles.forEach(profile => {
          this.selectedPermissions[profile.id] = this.transformPermissionsToTreeNodes(profile.permissions || []);
        });
        console.log("Profiles loaded: ", this.profiles);
        console.log("Selected permissions: ", this.selectedPermissions);
      }
    });
  }

  loadPermissions(): void {
    this.permissionService.getPermissions().subscribe((response: ApiResponse<Permission[]>) => {
      if (response.status === 'OK') {
        this.permissions = this.transformPermissionsToTreeNodes(response.data || []);
        console.log("Permissions loaded: ", this.permissions);
      }
    });
  }

  transformPermissionsToTreeNodes(permissions: Permission[]): TreeNode[] {
    return permissions.map(permission => ({
      label: permission.label,
      data: permission.id,
      expanded: true, // Expand all nodes by default
      children: this.transformPermissionsToTreeNodes(permission.children || [])
    }));
  }


  // updatePermissions(): void {
  //   this.profiles.forEach(profile => {
  //     const permissionIds = (this.selectedPermissions[profile.id] || []).map(node => node.data);
  //     this.profileService.updateProfilePermissions(profile.id, permissionIds).subscribe(response => {
  //       if (response.status === 'OK') {
  //         // Logic to handle successful update
  //         console.log(`Permissions updated for profile ${profile.name}`);
  //       }
  //     });
  //   });
  // }
}
