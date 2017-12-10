import {Component, ViewEncapsulation} from "@angular/core";
import {NgbModal, ModalDismissReasons, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';
import {ApiService} from "../../../services/api.service";
import {ExperimentService} from "../../../services/experiment.service";
import {Experiment} from "../../../models/experiment/experiment.model";
import {ApplicationIface} from "../../../models/application/application.iface.model";
import {ApplicationIfaceService} from "../../../services/application.iface.service";
import {ExperimentInput} from "../../../models/experiment/experiment.input.model";
import {ExperimentOutput} from "../../../models/experiment/experiment.output.model";
import {ApplicationDeployment} from "../../../models/application/application.deployment.model";
import {AppDeploymentService} from "../../../services/deployment.service";
import {ComputeResource} from "../../../models/compute/compute.resource.model";
import {ComputeService} from "../../../services/compute.service";
import {Router} from "@angular/router";
import {WorkFlow} from "../../../models/workflow/workflow.model";
import {WorkflowService} from "../../../services/workflow.service";

/**
 * Created by dimuthu on 10/29/17.
 */

@Component({
  templateUrl: './list.html',
  encapsulation: ViewEncapsulation.None,
  providers: [WorkflowService]
})
export class WorkflowListComponent {

  workFlows: Array<WorkFlow> = [];

  constructor(private workflowService: WorkflowService, private router: Router) {
    this.getAllWorkFlows();
  }

  routeToDetailPage(id: number) {
    this.router.navigateByUrl("/workflow/detail/"+id);
  }

  routeToCreatePage() {
    this.router.navigateByUrl("/workflow/create")
  }

  getAllWorkFlows() {
    this.workflowService.getAllWorkflows().subscribe(
      data => {this.workFlows = data},
      err => {console.log(err);});
  }

}
