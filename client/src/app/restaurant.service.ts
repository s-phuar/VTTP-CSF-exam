import { HttpClient, HttpHeaders, HttpParams } from "@angular/common/http";
import { inject } from "@angular/core";
import { firstValueFrom, Observable, Subject, tap } from "rxjs";
import { Combined, Menu, MenuSum, Order, Results } from "./models";

export class RestaurantService {

  private http = inject(HttpClient)
  private menuArray!: Menu[]    
  emitter = new Subject<Results>()


  public setArray(menus: Menu[]){
    this.menuArray = menus
  }

  public getArray(){
    return this.menuArray
  }


  // TODO: Task 2.2
  // You change the method's signature but not the name
  getMenuItems() : Promise<Menu[]>{
    return firstValueFrom(this.http.get<Menu[]>('/api/menu'))
  }

  // TODO: Task 3.2

  orderFood(order: Order): Observable<Results>{
    return this.http.post<Results>('/api/food_order', order)
      .pipe(
        tap(
            results => {
              this.emitter.next(results)
          }
        )
      )

  }


  makePayment(c: Combined): Observable<any>{
    return this.http.post<any>('/api/payment', c)
  } 


}
