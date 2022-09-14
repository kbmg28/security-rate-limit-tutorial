import { MatButtonModule } from '@angular/material/button';
import { ApiModule } from './_services/swagger/api.module';
import { environment } from './../environments/environment';
import { BASE_PATH } from './_services/swagger/variables';
import { AuthModule } from './auth/auth.module';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HomeComponent } from './home/home.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    AuthModule,
    BrowserAnimationsModule,
    ApiModule,
    HttpClientModule,
    MatButtonModule,
  ],
  providers: [
    {provide: BASE_PATH, useValue: environment.API_BASE_PATH},

  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
