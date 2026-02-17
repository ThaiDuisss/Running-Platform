   import {
       Bookmark,
       GearFill,
       Newspaper,
       People,
       ViewStacked
   } from 'react-bootstrap-icons'
   export const menuItems = {
       admin: [{
               to: '/',
               label: 'Dashboard',
               icon: ViewStacked,
               notification: 51111,
               end: true

           },
           {
               to: '/category',
               label: 'Category',
               icon: Bookmark,
               notification: 5

           },
           {
               to: '/news',
               label: 'News',
               icon: Newspaper,
               notification: 5

           },
           {
               to: '/users',
               label: 'Users',
               icon: People,
               notification: 5

           },
           {
               to: '/settings',
               label: 'Settings',
               icon: GearFill,
               notification: 5

           },
       ],
       instructor: [{
               to: '/category',
               label: 'Category',
               icon: Bookmark,
               notification: 5

           },
           {
               to: '/news',
               label: 'News',
               icon: Newspaper,
               notification: 5

           },
           {
               to: '/settings',
               label: 'Settings',
               icon: GearFill,
               notification: 5
           },
       ],
   }