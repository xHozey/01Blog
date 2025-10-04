export interface commentResponse {
  id: number;
  content: string;
  author: string;
  authorId: number;
  createTime: string;
  likes: number;
  isLiked: boolean;
  filePath: string;
}
