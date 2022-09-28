import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, Validators} from "@angular/forms";
import {AuthControllerService} from "../../_services/swagger";
import {Router} from "@angular/router";
import {ReCaptchaV3Service} from "ng-recaptcha";

@Component({
  selector: 'app-login-vulnerable',
  templateUrl: './login-vulnerable.component.html',
  styleUrls: ['./login-vulnerable.component.scss']
})
export class LoginVulnerableComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder,
              private authControllerService: AuthControllerService,
              private router: Router) {
    this.loginForm = this.fb.group({
      email: [null, [Validators.required, Validators.email]],
      password: [null, [Validators.required]]
    });
  }

  ngOnInit(): void {
  }

  get email() {   return this.loginForm.get('email'); }
  get password() {    return this.loginForm.get('password'); }

  getErrorEmailMessage() {
    if (this.email?.hasError('required')) {
      return 'Campo obrigatório';
    }

    return this.email?.hasError('email') ? 'Email inválido' : '';
  }

  onSubmit(): void {
    const email = this.email?.value;
    const password = this.password?.value;

    this.authControllerService.authenticate({email: email, password: password})
      .subscribe(res => {
        sessionStorage.setItem('tokenJwt', res.token || '');
        this.router.navigate(["/home"]);
      })
  }

}
