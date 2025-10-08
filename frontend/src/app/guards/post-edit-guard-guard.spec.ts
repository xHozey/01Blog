import { TestBed } from '@angular/core/testing';
import { CanActivateFn } from '@angular/router';

import { postEditGuardGuard } from './post-edit-guard-guard';

describe('postEditGuardGuard', () => {
  const executeGuard: CanActivateFn = (...guardParameters) => 
      TestBed.runInInjectionContext(() => postEditGuardGuard(...guardParameters));

  beforeEach(() => {
    TestBed.configureTestingModule({});
  });

  it('should be created', () => {
    expect(executeGuard).toBeTruthy();
  });
});
