import { NgFor, NgIf } from "@angular/common";
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, FormsModule, ReactiveFormsModule, Validators } from "@angular/forms";
import { AuthService } from "@core/auth/auth.service";
import { Profile } from "@core/models/profile.model";
import { Permission } from "@features/admin/models/permission.model";
import { PermissionService } from "@features/admin/services/permission.service";
import { ProfileService } from "@features/admin/services/profile.service";
import { ButtonComponent } from "@shared/components/button/button.component";
import { InputComponent } from "@shared/components/form/input/input.component";
import { ToastrService } from "ngx-toastr";
import { TreeNode } from "primeng/api";
import { TreeModule } from 'primeng/tree';
import { TreeSelectModule } from 'primeng/treeselect';


@Component({
  selector: 'app-authorizations',
  standalone: true,
  imports: [NgIf, ReactiveFormsModule, FormsModule, TreeModule, TreeSelectModule, NgFor, InputComponent, ButtonComponent],
  templateUrl: './authorizations.component.html',
})
export class AuthorizationsComponent implements OnInit {
  newProfileForm: FormGroup;
  profiles: Profile[] = [];
  treeData: { [key: string]: TreeNode[] } = {};
  nodes: TreeNode[] = [];
  selectedPermissions: TreeNode[] = [];
  selectedNodes: { [key: string]: TreeNode[] } = {};
  userProfile: string | null = null;

  constructor(
    private fb: FormBuilder,
    private toastr: ToastrService,
    private profileService: ProfileService,
    private authService: AuthService,
    private permissionService: PermissionService
  ) {
    this.newProfileForm = this.fb.group({
      name: ['', Validators.required],
      permissions: new FormControl([])
    });
  }

  ngOnInit() {
    this.loadProfilesAndPermissions();
    this.authService.getUser().subscribe(currentUser => {
      this.userProfile = currentUser?.profile.name || null;
    });
  }

  loadProfilesAndPermissions() {
    this.profileService.getProfiles().subscribe(profileResponse => {
      if (profileResponse.status === 'OK') {
        this.profiles = profileResponse.data || []
        this.permissionService.getPermissionTree().subscribe(permissionResponse => {
          if (permissionResponse.status === 'OK') {
            const allPermissions = permissionResponse.data;
            this.nodes = this.convertPermissionsToTreeNodes(allPermissions || [], []);
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

  onPermissionChange(event: any, profileName: string | undefined): void {
    if (!profileName) {
      this.toastr.error('Le nom du profil est manquant.');
      return;
    }

    this.selectedNodes[profileName] = event;
    this.updateNodeSelectionState(this.treeData[profileName], profileName);
  }

  updateNodeSelectionState(treeNodes: TreeNode[], profileName: string): void {
    if (!Array.isArray(this.selectedNodes[profileName])) {
      this.selectedNodes[profileName] = [];
    }

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

  addProfile() {
    if (this.newProfileForm.valid) {
      const profileAddRequest = {
        name: this.newProfileForm.value.name,
        permissionIds: this.newProfileForm.value.permissions.map((node: TreeNode) => parseInt(node.key!, 10))
      };
      this.profileService.addProfile(profileAddRequest).subscribe(response => {
        if (response.status === 'CREATED') {
          this.toastr.success('Profil ajouté avec succès');
          this.profiles.push(response.data!);
          this.newProfileForm.reset();
          this.newProfileForm.patchValue({ permissions: [] });
          this.loadProfilesAndPermissions();
        } else {
          this.toastr.error('Une erreur s\'est produite lors de l\'ajout du profil');
        }
      });
    }
  }

  updateProfile(profile: Profile): void {
    if (!profile.name) {
      this.toastr.error('Le nom du profil est manquant.');
      return;
    }

    const profileEditRequest = {
      permissionIds: this.selectedNodes[profile.name]?.map(node => parseInt(node.key!, 10)) || []
    };

    this.profileService.updateProfile(profile.id, profileEditRequest).subscribe(response => {
      if (response.status === 'OK') {
        this.toastr.success('Permissions mises à jour avec succès');
        this.loadProfilesAndPermissions(); // Recharge les profils et les permissions
      } else {
        this.toastr.error('Une erreur s\'est produite lors de la mise à jour des permissions du profil');
      }
    });
  }
}