import {OperationInPort} from "./operation.inport.type.model";
import {OperationOutPort} from "./operation.outport.type.model";

export class OperationType {
  id: number;
  name: string;
  icon: string = "assets/icons/ssh.png";
  inPorts: Array<OperationInPort> = [];
  outPorts: Array<OperationOutPort> = [];

  clone(): OperationType {
    let cloned: OperationType = new OperationType();
    cloned.id = this.id;
    cloned.name = this.name;
    cloned.icon = this.icon;

    this.inPorts.forEach((inPort) => {
      let clonedInPort: OperationInPort = new OperationInPort(inPort.id, inPort.name);
      cloned.inPorts.push(clonedInPort);
    });

    this.outPorts.forEach((outPort) => {
      let clonedOutPort: OperationOutPort = new OperationOutPort(outPort.id, outPort.name);
      cloned.outPorts.push(clonedOutPort);
    });

    return cloned;
  }
}
