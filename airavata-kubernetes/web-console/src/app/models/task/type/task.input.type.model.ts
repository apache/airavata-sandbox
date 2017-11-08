export class TaskInputType {
  id: number;
  name: String;
  type: String;
  defaultValue: string;


  constructor(id: number = 0, name: String = null, type: String = null, defaultValue: string = null) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.defaultValue = defaultValue;
  }
}
