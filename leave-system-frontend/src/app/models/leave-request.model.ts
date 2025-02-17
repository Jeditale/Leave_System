import { LeaveType } from "./leave-Type.model";
import { User } from "./user.model";

export interface LeaveRequest {
  id?: number
  user:User
  leaveType:LeaveType
  startDate:string
  endDate:string
  status:string
  reason:string
  comment?:string
}
