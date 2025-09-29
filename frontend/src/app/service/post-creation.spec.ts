import { TestBed } from '@angular/core/testing';

import { PostCreation } from './post-creation';

describe('PostCreation', () => {
  let service: PostCreation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PostCreation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
