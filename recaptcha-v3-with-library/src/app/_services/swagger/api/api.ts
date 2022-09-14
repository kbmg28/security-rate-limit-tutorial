export * from './authController.service';
import { AuthControllerService } from './authController.service';
export * from './infoController.service';
import { InfoControllerService } from './infoController.service';
export * from './testController.service';
import { TestControllerService } from './testController.service';
export const APIS = [AuthControllerService, InfoControllerService, TestControllerService];
