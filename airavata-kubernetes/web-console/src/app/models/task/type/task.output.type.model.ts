export class TaskOutputType {
  id: number;
  name: string;
  type: string;


  constructor(id: number = 0, name: string = null, type: string = null) {
    this.id = id;
    this.name = name;
    this.type = type;
  }
}
