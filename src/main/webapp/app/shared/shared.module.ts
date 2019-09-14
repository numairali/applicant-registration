import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {
  ApplicantRegistrationSharedLibsModule,
  ApplicantRegistrationSharedCommonModule,
  JhiLoginModalComponent,
  HasAnyAuthorityDirective
} from './';

@NgModule({
  imports: [ApplicantRegistrationSharedLibsModule, ApplicantRegistrationSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [ApplicantRegistrationSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class ApplicantRegistrationSharedModule {
  static forRoot() {
    return {
      ngModule: ApplicantRegistrationSharedModule
    };
  }
}
