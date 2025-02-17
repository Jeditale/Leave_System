export interface CreateRequest{
  user:{id:number}
  leaveType:{id:number}
  startDate:string
  endDate:string
  status:string
  reason: string
  comment?:string
}
