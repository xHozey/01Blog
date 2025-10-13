export function parseApiError(err: any): string[] {
  const messages: string[] = [];

  let apiError: any;

  try {
    apiError = typeof err.error === 'string' ? JSON.parse(err.error) : err.error;
  } catch {
    messages.push(err.error || err.message || 'Unexpected error');
    return messages;
  }

  if (apiError.fields) {
    for (const [field, msg] of Object.entries(apiError.fields)) {
      messages.push(`${field}: ${msg}`);
    }
  }

  if (apiError.error) {
    messages.push(apiError.error);
  }

  return messages.length ? messages : ['Unexpected error'];
}
