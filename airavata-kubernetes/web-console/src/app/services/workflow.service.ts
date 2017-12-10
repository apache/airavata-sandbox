import {Inject, Injectable} from "@angular/core";
import {ApiService} from "./api.service";

@Injectable()
export class WorkflowService {

  constructor(@Inject(ApiService) private apiService:ApiService) {
  }

  createWorkflow(name: string, xml: any) {
    return this.apiService.post("workflow/create/" + name, xml)
  }

  getAllWorkflows() {
    return this.apiService.get("workflow").map(res => res.json());
  }

  getWorkflowById(id: number) {
    return this.apiService.get("workflow/" + id).map(res => res.json());
  }

  launchWorkflow(id: number) {
    return this.apiService.get("workflow/" + id + "/launch");
  }
}
