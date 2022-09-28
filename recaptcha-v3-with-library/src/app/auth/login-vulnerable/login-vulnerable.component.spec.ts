import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LoginVulnerableComponent } from './login-vulnerable.component';

describe('LoginVulnerableComponent', () => {
  let component: LoginVulnerableComponent;
  let fixture: ComponentFixture<LoginVulnerableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ LoginVulnerableComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LoginVulnerableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
