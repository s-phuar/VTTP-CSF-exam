import { Component, inject, OnInit } from '@angular/core';
import { RestaurantService } from '../restaurant.service';
import { Menu } from '../models';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';

@Component({
  selector: 'app-menu',
  standalone: false,
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.css'
})
export class MenuComponent implements OnInit{
  // TODO: Task 2
  private router = inject(Router)
  private resSvc = inject(RestaurantService)

  menus$ !: Promise<Menu[]>
  menuArray !: Menu[] //quantity is not defined
  totalCount : number = 0
  totalAmount : number = 0

  ngOnInit(): void {
    this.resSvc.getMenuItems() //receives promise
      .then((result) => {
        this.menuArray = result
        // console.info('starting', this.menuArray)
      })

  }

  order(){
    this.resSvc.setArray(this.menuArray)
    this.router.navigate(['/order'])
  }

  validChk() : boolean{
    return this.totalCount==0
  }

  updateQty(id: string, delta: number){
    const indexToUpdate = this.menuArray.findIndex(m => m.id === id)
    let oldNumber !: number
    if(isNaN(this.menuArray[indexToUpdate].quantity) || this.menuArray[indexToUpdate].quantity == undefined){
      oldNumber = 0
    }else{
      oldNumber = this.menuArray[indexToUpdate].quantity
    }

    const newMenu: Menu = {
      id: this.menuArray[indexToUpdate].id,
      name: this.menuArray[indexToUpdate].name,
      description: this.menuArray[indexToUpdate].description,
      price: this.menuArray[indexToUpdate].price,
      quantity: oldNumber! += delta,
    }

    if(oldNumber < 0  && delta == -1){
      console.info('cant deduct')
    }else{
      this.menuArray.splice(indexToUpdate, 1, newMenu)
      this.totalCount += delta
      this.totalAmount = this.totalAmount + delta * this.menuArray[indexToUpdate].price
    }

    // console.info('end array', this.menuArray);

    }


  }



