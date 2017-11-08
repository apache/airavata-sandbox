import {TaskInputType} from "./task.input.type.model";
import {TaskOutputType} from "./task.output.type.model";
import {TaskOutPort} from "./task.outport.model";
export class TaskType {

  id: number;
  name: string;
  topicName: string;
  icon: string = "assets/icons/ssh.png";
  inputTypes: Array<TaskInputType> = [];
  outputTypes: Array<TaskOutputType> = [];
  outPorts: Array<TaskOutPort> = [];

  clone(): TaskType {
    let cloned: TaskType = new TaskType();
    cloned.id = this.id;
    cloned.name = this.name;
    cloned.icon = this.icon;
    cloned.topicName = this.topicName;

    this.inputTypes.forEach((input) => {
      let clonedInput: TaskInputType = new TaskInputType(input.id, input.name, input.type, input.defaultValue);
      cloned.inputTypes.push(clonedInput);
    });

    this.outputTypes.forEach((output) => {
      let clonedOutput: TaskOutputType = new TaskOutputType(output.id, output.name, output.type);
      cloned.outputTypes.push(clonedOutput);
    });

    this.outPorts.forEach((outPort) => {
      let clonedOutPort: TaskOutPort = new TaskOutPort(outPort.id, outPort.name, outPort.order);
      cloned.outPorts.push(clonedOutPort);
    });

    return cloned;
  }

}
