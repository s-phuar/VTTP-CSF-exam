import { Component, inject, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Combined, Menu, MenuSum, Order, Results } from '../models';
import { RestaurantService } from '../restaurant.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-place-order',
  standalone: false,
  templateUrl: './place-order.component.html',
  styleUrl: './place-order.component.css'
})
export class PlaceOrderComponent implements OnInit{

  private resSvc = inject(RestaurantService)
  private router = inject(Router)
  private fb = inject(FormBuilder)
  form!: FormGroup

  // TODO: Task 3
  menuArray !: Menu[]
  menuSummary : MenuSum[] = []
  totalAmount : number = 0
  sub !: Subscription
  results!: Results

  // orderConfirmed$ !: Observable<Results>

  ngOnInit(): void {
    this.menuArray = this.resSvc.getArray()
    this.display()
    this.createForm()
  }


  submitForm(){
    for(let m of this.menuArray){
      let ms: MenuSum = {
        id: m.id,
        price: m.price,
        quantity: m.quantity
      }
      this.menuSummary.push(ms)
    }

    const order: Order = {
      username: this.form.controls['username'].value,
      password: this.form.controls['password'].value,
      items: this.menuSummary
    }

    this.sub = this.resSvc.orderFood(order).subscribe({
      next: resp => {
        this.results = resp
        console.info('this.results: ', resp) //Results type
        this.payment()
      },
      error: err => console.info(err.error)
    })
  }



  payment(){
    // this.orderConfirmed$ = this.resSvc.emitter.asObservable()

    const c:Combined = {
      results: this.results,
      menu: this.menuSummary
    }

    this.resSvc.makePayment(c).subscribe({
      next: resp =>{
        console.info('making payment', resp)
      },
      error: err => {console.info(err.error)}
    })

  }


  startOver(){
    this.router.navigate(['/'])
  }


  invalid(): boolean{
    return this.form.invalid
  }
  createForm(){
    this.form = this.fb.group({
      username:this.fb.control<string>('', [Validators.required]),
      password:this.fb.control<string>('', [Validators.required]),
    })
  }


  display(){
    this.menuArray = this.menuArray.filter(m => m.quantity > 0 && m.quantity != null && m.quantity != undefined) //filter non-chosen items
    console.info('test', this.menuArray);
    for(let m  of this.menuArray){
      this.totalAmount += (m.price*m.quantity)
    }
    
  }


}
