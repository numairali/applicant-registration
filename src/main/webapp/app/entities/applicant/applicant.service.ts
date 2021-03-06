import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IApplicant } from 'app/shared/model/applicant.model';

type EntityResponseType = HttpResponse<IApplicant>;
type EntityArrayResponseType = HttpResponse<IApplicant[]>;

@Injectable({ providedIn: 'root' })
export class ApplicantService {
  public resourceUrl = SERVER_API_URL + 'api/applicants';

  constructor(protected http: HttpClient) {}

  create(applicant: IApplicant): Observable<EntityResponseType> {
    return this.http.post<IApplicant>(this.resourceUrl, applicant, { observe: 'response' });
  }

  update(applicant: IApplicant): Observable<EntityResponseType> {
    return this.http.put<IApplicant>(this.resourceUrl, applicant, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IApplicant>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IApplicant[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
