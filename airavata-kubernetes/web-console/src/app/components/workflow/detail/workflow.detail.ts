import {ViewEncapsulation, Component} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {NgbModal, NgbModalRef} from "@ng-bootstrap/ng-bootstrap";
import {Process} from "../../../models/process/process.model";
import {ProcessService} from "../../../services/process.service";
import {ProcessStatus} from "../../../models/process/process.status.model";
import {WorkFlow} from "../../../models/workflow/workflow.model";
import {WorkflowService} from "../../../services/workflow.service";

/**
 * Created by dimuthu on 10/29/17.
 */

@Component({
  templateUrl: './detail.html',
  encapsulation: ViewEncapsulation.None,
  providers: [WorkflowService, ProcessService]
})
export class WorkflowDetailComponent {

  selectedWorkflow: WorkFlow = new WorkFlow();
  processes: Array<Process> = [];
  processLastState: ProcessStatus = new ProcessStatus();
  processListModel: NgbModalRef;
  workflowId: number;

  constructor(private modalService: NgbModal,private activatedRoute: ActivatedRoute,
              private workflowService: WorkflowService, private processService: ProcessService,
              private router: Router) {

    this.workflowId = this.activatedRoute.snapshot.params["id"];
    this.fetchWorkflow(this.workflowId);
  }

  launchWorkflow() {
    this.workflowService.launchWorkflow(this.selectedWorkflow.id).subscribe(data => {
      alert("Workflow launching started");
    },
      err => {
        console.log(err);
        alert("Workflow launch failed");
      }
    )
  }

  routeToProcessPage(id: number) {
    this.processListModel.close();
    this.router.navigateByUrl("/process/detail/" + id);
  }

  openAsModel(content) {
    this.modalService.open(content, {size: "lg"}).result.then((result) => {}, (reason) => {});
  }

  openProcessesAsModel(content) {
    this.processes = [];

    this.workflowService.getWorkflowById(this.workflowId)
      .subscribe(data => {

        this.selectedWorkflow = data;
        this.selectedWorkflow.processIds.forEach(id => {

          this.processService.getProcessById(id).subscribe(data => {
            this.processes.push(data);
            this.processes.sort((p1, p2) => {return p1.id - p2.id;})

          }, err => {
            console.log(err);
          });

        });

        this.processListModel = this.modalService.open(content, {size: "lg"});
        this.processListModel.result.then((result) => {}, (reason) => {});

      }, err => {console.log(err)});

  }

  fetchWorkflow(id: number) {
    this.workflowService.getWorkflowById(id)
      .subscribe(data => {this.selectedWorkflow = data}, err => {console.log(err)});
  }
}
