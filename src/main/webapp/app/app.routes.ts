import { inject } from '@angular/core';
import { ActivatedRouteSnapshot, Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';
import { RoleListComponent } from './role/role-list.component';
import { RoleAddComponent } from './role/role-add.component';
import { RoleEditComponent } from './role/role-edit.component';
import { UserListComponent } from './user/user-list.component';
import { UserAddComponent } from './user/user-add.component';
import { UserEditComponent } from './user/user-edit.component';
import { PostListComponent } from './post/post-list.component';
import { PostAddComponent } from './post/post-add.component';
import { PostEditComponent } from './post/post-edit.component';
import { CommentListComponent } from './comment/comment-list.component';
import { CommentAddComponent } from './comment/comment-add.component';
import { CommentEditComponent } from './comment/comment-edit.component';
import { ReclamationListComponent } from './reclamation/reclamation-list.component';
import { ReclamationAddComponent } from './reclamation/reclamation-add.component';
import { ReclamationEditComponent } from './reclamation/reclamation-edit.component';
import { PostReactionListComponent } from './post-reaction/post-reaction-list.component';
import { PostReactionAddComponent } from './post-reaction/post-reaction-add.component';
import { PostReactionEditComponent } from './post-reaction/post-reaction-edit.component';
import { CommentReactionListComponent } from './comment-reaction/comment-reaction-list.component';
import { CommentReactionAddComponent } from './comment-reaction/comment-reaction-add.component';
import { CommentReactionEditComponent } from './comment-reaction/comment-reaction-edit.component';
import { AuthenticationComponent } from './security/authentication.component';
import { RegistrationComponent } from './security/registration.component';
import { ErrorComponent } from './error/error.component';
import { AuthenticationService, ADMIN, CLIENT } from 'app/security/authentication.service';


export const routes: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: $localize`:@@home.index.headline:Welcome to your new app!`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'roles',
    component: RoleListComponent,
    title: $localize`:@@role.list.headline:Roles`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'roles/add',
    component: RoleAddComponent,
    title: $localize`:@@role.add.headline:Add Role`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'roles/edit/:id',
    component: RoleEditComponent,
    title: $localize`:@@role.edit.headline:Edit Role`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'users',
    component: UserListComponent,
    title: $localize`:@@user.list.headline:Users`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'users/add',
    component: UserAddComponent,
    title: $localize`:@@user.add.headline:Add User`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'users/edit/:id',
    component: UserEditComponent,
    title: $localize`:@@user.edit.headline:Edit User`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'posts',
    component: PostListComponent,
    title: $localize`:@@post.list.headline:Posts`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'posts/add',
    component: PostAddComponent,
    title: $localize`:@@post.add.headline:Add Post`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'posts/edit/:id',
    component: PostEditComponent,
    title: $localize`:@@post.edit.headline:Edit Post`,
    data: {
      roles: [ADMIN]
    }
  },
  {
    path: 'comments',
    component: CommentListComponent,
    title: $localize`:@@comment.list.headline:Comments`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'comments/add',
    component: CommentAddComponent,
    title: $localize`:@@comment.add.headline:Add Comment`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'comments/edit/:id',
    component: CommentEditComponent,
    title: $localize`:@@comment.edit.headline:Edit Comment`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'reclamations',
    component: ReclamationListComponent,
    title: $localize`:@@reclamation.list.headline:Reclamations`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'reclamations/add',
    component: ReclamationAddComponent,
    title: $localize`:@@reclamation.add.headline:Add Reclamation`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'reclamations/edit/:id',
    component: ReclamationEditComponent,
    title: $localize`:@@reclamation.edit.headline:Edit Reclamation`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'postReactions',
    component: PostReactionListComponent,
    title: $localize`:@@postReaction.list.headline:Post Reactions`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'postReactions/add',
    component: PostReactionAddComponent,
    title: $localize`:@@postReaction.add.headline:Add Post Reaction`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'postReactions/edit/:id',
    component: PostReactionEditComponent,
    title: $localize`:@@postReaction.edit.headline:Edit Post Reaction`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'commentReactions',
    component: CommentReactionListComponent,
    title: $localize`:@@commentReaction.list.headline:Comment Reactions`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'commentReactions/add',
    component: CommentReactionAddComponent,
    title: $localize`:@@commentReaction.add.headline:Add Comment Reaction`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'commentReactions/edit/:id',
    component: CommentReactionEditComponent,
    title: $localize`:@@commentReaction.edit.headline:Edit Comment Reaction`,
    data: {
      roles: [ADMIN, CLIENT]
    }
  },
  {
    path: 'login',
    component: AuthenticationComponent,
    title: $localize`:@@authentication.login.headline:Login`
  },
  {
    path: 'register',
    component: RegistrationComponent,
    title: $localize`:@@registration.register.headline:Registration`
  },
  {
    path: 'error',
    component: ErrorComponent,
    title: $localize`:@@error.headline:Error`
  },
  {
    path: '**',
    component: ErrorComponent,
    title: $localize`:@@notFound.headline:Page not found`
  }
];

// add authentication check to all routes
for (const route of routes) {
  route.canActivate = [(route: ActivatedRouteSnapshot) => inject(AuthenticationService).checkAccessAllowed(route)];
}
