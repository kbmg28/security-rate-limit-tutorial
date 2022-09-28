import { ReCaptchaV3Service } from 'ng-recaptcha';
import { Router } from '@angular/router';
import { AuthControllerService } from './../../_services/swagger/api/authController.service';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder,
    private authControllerService: AuthControllerService,
    private router: Router,
    private reCaptchaV3Service: ReCaptchaV3Service) {
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

    this.reCaptchaV3Service.execute('login').subscribe(recaptchaToken => {
      this.authControllerService.authenticateWithRecaptcha({email: email, password: password}, recaptchaToken)
      .subscribe(res => {
        sessionStorage.setItem('tokenJwt', res.token || '');
        this.router.navigate(["/home"]);
      })
    })
  }

}
