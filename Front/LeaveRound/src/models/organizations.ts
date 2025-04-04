export type TOrgsCategory = '전체' | '환경' | '보건' | '교육' | '기타';

export interface TOrganization {
  organizationId: number;
  organizationName: string;
  address: string;
  representative: string;
  homepageUrl: string;

  organizationProjectId: number;
  goalAmount: number;
  totalUseAmount: number;
  currentAmount: number;

  projectTitle: string;
  projectContent: string;
  projectCategory: TOrgsCategory;
  projectStatus: string;

  createdAt: string;
  updatedAt: string;
  endDatedAt: string;
}
