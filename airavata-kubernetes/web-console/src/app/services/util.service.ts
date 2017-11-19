import {Injectable} from "@angular/core";

@Injectable()
export class UtilService {

  toDateString(timestamp: number) {
    return new Date(timestamp).toString();
  }

  toTimeString(timestamp: number) {
    return new Date(timestamp).toLocaleTimeString();
  }
}
