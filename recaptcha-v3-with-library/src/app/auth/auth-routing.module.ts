import { LoginComponent } from './login/login.component';
import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {LoginVulnerableComponent} from "./login-vulnerable/login-vulnerable.component";

const routes: Routes = [

  { path: 'login', component: LoginComponent },
  { path: 'login-vulnerable', component: LoginVulnerableComponent },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AuthRoutingModule { }
