export class TaskOutPort {
  id: number;
  name: string;
  order: number;


  constructor(id: number =0 , name: string = null, order: number = 0) {
    this.id = id;
    this.name = name;
    this.order = order;
  }
}
