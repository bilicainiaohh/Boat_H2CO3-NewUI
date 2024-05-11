#include "../gles.h"

//GLint gl4es_glRenderMode(GLenum mode);
//void gl4es_glBlendEquationSeparate(GLenum modeRGB, GLenum modeAlpha);
void gl4es_glBlendEquationSeparatei(GLuint buf, GLenum modeRGB, GLenum modeAlpha);

//void gl4es_glBlendFuncSeparate(GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha, GLenum dstAlpha);
void gl4es_glBlendFuncSeparatei(GLuint buf, GLenum srcRGB, GLenum dstRGB, GLenum srcAlpha,
                                GLenum dstAlpha);

//void gl4es_glColorMaterial(GLenum face, GLenum mode);
void gl4es_glCopyPixels(GLint x, GLint y, GLsizei width, GLsizei height, GLenum type);

void gl4es_glDrawBuffer(GLenum mode);

void gl4es_glEdgeFlag(GLboolean flag);

//void gl4es_glFogCoordd(GLdouble coord);
//void gl4es_glFogCoorddv(const GLdouble *coord);
//void gl4es_glFogCoordf(GLfloat coord);
//void gl4es_glFogCoordfv(const GLfloat *coord);
//void gl4es_glGetTexImage(GLenum target, GLint level, GLenum format, GLenum type, GLvoid * img);
//void gl4es_glGetTexLevelParameterfv(GLenum target, GLint level, GLenum pname, GLfloat *params);
//void gl4es_glGetTexLevelParameteriv(GLenum target, GLint level, GLenum pname, GLint *params);
void gl4es_glIndexf(GLfloat c);

void gl4es_glLightModeli(GLenum pname, GLint param);

void gl4es_glPolygonStipple(const GLubyte *mask);

void gl4es_glReadBuffer(GLenum mode);

void gl4es_glSecondaryColor3f(GLfloat r, GLfloat g, GLfloat b);

void
gl4es_glColorTable(GLenum target, GLenum internalformat, GLsizei width, GLenum format, GLenum type,
                   const GLvoid *table);
//void gl4es_glIndexPointer(GLenum  type,  GLsizei  stride,  const GLvoid *  pointer);

void gl4es_glAccum(GLenum op, GLfloat value);

void gl4es_glPrioritizeTextures(GLsizei n, const GLuint *textures, const GLclampf *priorities);

void gl4es_glPixelMapfv(GLenum map, GLsizei mapsize, const GLfloat *values);

void gl4es_glPixelMapuiv(GLenum map, GLsizei mapsize, const GLuint *values);

void gl4es_glPixelMapusv(GLenum map, GLsizei mapsize, const GLushort *values);

void gl4es_glPassThrough(GLfloat token);

void gl4es_glIndexMask(GLuint mask);

void gl4es_glGetPixelMapfv(GLenum map, GLfloat *data);

void gl4es_glGetPixelMapuiv(GLenum map, GLuint *data);

void gl4es_glGetPixelMapusv(GLenum map, GLushort *data);

void gl4es_glClearIndex(GLfloat c);

void gl4es_glGetPolygonStipple(GLubyte *pattern);

void gl4es_glFeedbackBuffer(GLsizei size, GLenum type, GLfloat *buffer);

void gl4es_glEdgeFlagv(GLboolean *flag);
