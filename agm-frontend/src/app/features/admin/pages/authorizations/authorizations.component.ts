import { Component } from '@angular/core';
import { NodeService } from "@core/services/node.service";
import { TreeNode } from "primeng/api";
import { TreeModule } from 'primeng/tree';

@Component({
  selector: 'app-authorizations',
  standalone: true,
  imports: [TreeModule],
  providers: [NodeService],
  templateUrl: './authorizations.component.html',
  styleUrls: ['./authorizations.component.css']
})
export class AuthorizationsComponent {
  files!: TreeNode[];
  selectedFiles: TreeNode[] = [];

  constructor(private nodeService: NodeService) { }

  ngOnInit() {
    this.nodeService.getFiles().then((data) => {
      this.files = data;
      this.expandAll(this.files);
    });
  }

  expandAll(nodes: TreeNode[]): void {
    nodes.forEach(node => {
      node.expanded = true;
      if (node.children) {
        this.expandAll(node.children);
      }
    });
  }

  toggleNodeSelection(node: TreeNode, event: Event) {
    const index = this.selectedFiles.indexOf(node);
    if (index >= 0) {
      this.selectedFiles.splice(index, 1);
    } else {
      this.selectedFiles.push(node);
    }
    event.stopPropagation();
  }
}
