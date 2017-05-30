package GraphAlgorithms;


public class Quadratic_Assignment_Problem{
	
	public Quadratic_Assignment_Problem(){
	}


public static void quadraticAssignment(int n, int a[][], int b[][], int sol[]) 
{ 
  int valc,vald,valf,valg,valh,vali,valk,valp,valq,valw,valx,valy; 
  int i,j,k,p,rowdist,rowweight,rowmata,rowmatb,partialobj; 
  int leastw,leastx,leasty,matbx,matby,matcx,matcy,matax; 
  int bestobjval,ntwo,npp,large; 
  int vale=0,subcripta=0,subcriptb=0,subcriptc=0,subcriptd=0,partialz=0; 
  int parm1[] = new int[1]; 
  int parm2[] = new int[1]; 
  int parm3[] = new int[1]; 
  int aux2[] = new int[n-1]; 
  int aux3[] = new int[n-1]; 
  int aux4[] = new int[n-1]; 
  int aux5[] = new int[n-1]; 
  int aux6[] = new int[n-1]; 
  int aux7[] = new int[n+1]; 
  int aux8[] = new int[n+1]; 
  int aux9[] = new int[n+1]; 
  int aux10[] = new int[n+1]; 
  int aux11[] = new int[n+1]; 
  int aux12[] = new int[n+1]; 
  int aux13[] = new int[n+1]; 
  int aux14[] = new int[n+1]; 
  int aux15[] = new int[n*n+1]; 
  int aux16[] = new int[n*(n+1)*(2*n-2)/6 + 1]; 
  int aux17[] = new int[n*(n+1)*(2*n-2)/6 + 1]; 
  int aux18[] = new int[(n*(n+1)*(2*n+1)/6)]; 
  int aux19[][] = new int[n+1][n+1]; 
  int aux20[][] = new int[n+1][n+1]; 
  boolean work1[] = new boolean[n+1]; 
  boolean work2[] = new boolean[n+1]; 
  boolean work3[] = new boolean[n+1]; 
  boolean contr,skip=false; 
 
  // initialization 
  p = 1; 
  k = 1; 
  for (i=1; i<=n; i++) { 
    for (j=1; j<=n; j++) { 
      p += a[i][j]; 
      k += b[i][j]; 
      aux19[i][j] = 0; 
    } 
    aux13[i] = 0; 
  } 
  large = n * p * k; 
  k = 0; 
  bestobjval = large; 
  ntwo = n - 2; 
  valy = n + 1; 
  j = 0; 
  valp = 0; 
  for (i=1; i<=ntwo; i++) { 
    npp = valy - i; 
    valq = npp * npp; 
    j += valq - npp; 
    aux4[i] = j; 
    valp += valq; 
    aux5[i] = valp; 
  } 
  // compute a[i][i] * b[j][j] 
  for (i=1; i<=n; i++) { 
    work1[i] = false; 
    work2[i] = false; 
    partialz = a[i][i]; 
    for (j=1; j<=n; j++) { 
      aux20[i][j] = -1; 
      aux19[i][j] += partialz * b[j][j]; 
    } 
  } 
  for (j=1; j<=n; j++) { 
    a[j][j] = large; 
    b[j][j] = large; 
  } 
  // reduce matrices a and b 
  for (i=1; i<=n; i++) { 
    rowmata = a[i][1]; 
    rowmatb = b[i][1]; 
    for (j=2; j<=n; j++) { 
      rowdist = a[i][j]; 
      rowweight = b[i][j]; 
      if (rowdist < rowmata) rowmata = rowdist; 
      if (rowweight < rowmatb) rowmatb = rowweight; 
    } 
    for (j=1; j<=n; j++) { 
      a[i][j] -= rowmata; 
      b[i][j] -= rowmatb; 
    } 
    aux7[i] = rowmata; 
    aux8[i] = rowmatb; 
  } 
  // rowwise reduction of matrices a and b 
  for (i=1; i<=n; i++) { 
    matcy = aux7[i]; 
    for (j=1; j<=n; j++) { 
      matbx = aux8[j]; 
      matby = (n-1) * matbx; 
      for (p=1; p<=n; p++) 
        if (p != j) matby += b[j][p]; 
      matcx = matcy * matby; 
      matax = 0; 
      for (p=1; p<=n; p++) 
        if (p != i) matax += a[i][p]; 
      aux19[i][j] += matcx + matbx * matax; 
    } 
  } 
  // columnwise reduction of matrices a and b 
  for (i=1; i<=n; i++) { 
    rowmata = a[1][i]; 
    rowmatb = b[1][i]; 
    for (j=2; j<=n; j++) { 
      rowdist = a[j][i]; 
      rowweight = b[j][i]; 
      if (rowdist < rowmata) rowmata = rowdist; 
      if (rowweight < rowmatb) rowmatb = rowweight; 
    } 
     for (j=1; j<=n; j++) { 
       a[j][i] -= rowmata; 
       b[j][i] -= rowmatb; 
    } 
    aux7[i] = rowmata; 
    aux8[i] = rowmatb; 
  } 
  for (i=1; i<=n; i++) { 
    a[i][i] = 0; 
    b[i][i] = 0; 
    matcy = aux7[i]; 
    for (j=1; j<=n; j++) { 
      matbx = aux8[j]; 
      matby = (n-1) * matbx; 
      for (p=1; p<=n; p++) 
        if (p != j) matby += b[p][j]; 
      matcx = matcy * matby; 
      matax = 0; 
      for (p=1; p<=n; p++) 
        if (p != i) matax += a[p][i]; 
      aux19[i][j] += matcx + matbx * matax; 
    } 
  } 
  partialobj = 0; 
  npp = n; 
  contr = true; 
  // compute minimal scalar products 
  qapsubprog2(n,k,npp,subcripta,subcriptb,a,b,aux4,aux7, 
              work1,work2,aux16,aux17,aux13); 
  qapsubprog3(n,k,npp,aux15,aux4,aux5,contr,aux16,aux17,aux18); 
  contr = false; 
  iterate: 
  while (true) { 
    valx = 0; 
    valy = 0; 
    valq = 0; 
    valf = 0; 
    for (i=1; i<=n; i++) 
      if (!work1[i]) { 
        valc = valx * npp; 
        valx++; 
        vald = 0; 
        for (j=1; j<=n; j++) 
          if (!work2[j]) { 
            vald++; 
            valc++; 
            if (aux20[i][j] < 0) 
              aux15[valc] += aux19[i][j]; 
            else { 
              aux15[valc] = large; 
              valf++; 
              if (valf < 2) { 
                subcriptc = valx; 
                subcriptd = vald; 
              } 
              else { 
                if (valf == 2) { 
                  if (valx == subcriptc) valy = valx; 
                  if (vald == subcriptd) valq = vald; 
                } 
              } 
            } 
          } 
      } 
    // obtain a bound by solving the linear assignment problem 
    skip = false; 
    obtainbound: 
    while (true) { 
      if (!skip) { 
        qapsubprog4(npp,large,aux15,parm1,aux14,aux12,aux11, 
                    aux9,aux7,aux8,aux13,work3); 
        partialz = parm1[0]; 
        valc = 0; 
        for (i=1; i<=npp; i++) 
          for (j=1; j<=npp; j++) { 
            valc++; 
            aux15[valc] -= (aux7[i] + aux8[j]); 
          } 
        if (partialobj + partialz >= bestobjval) { 
          // backtrack 
          if (!contr) { 
            if (k == 0) { 
              return; 
            } 
            subcripta = aux2[k]; 
            subcriptb = aux3[k]; 
          } 
          else { 
            contr = false; 
            k++; 
            // cancel the last single assignment 
            for (i=1; i<=n; i++) 
              if (!work1[i]) { 
                for (j=1; j<=n; j++) 
                  if (!work2[j] && aux20[i][j] == k) aux20[i][j] = -1; 
              } 
            partialobj -= aux19[subcripta][subcriptb]; 
            work1[subcripta] = false; 
            work2[subcriptb] = false; 
            k--; 
            npp = n - k; 
            if (aux6[k+1] + partialobj >= bestobjval) { 
              if (k == 0) { 
                return; 
              } 
              subcripta = aux2[k]; 
              subcriptb = aux3[k]; 
            } 
            else { 
              qapsubprog3(n,k,npp,aux15,aux4,aux5,contr,aux16,aux17,aux18); 
              continue iterate; 
            } 
          } 
          skip = true; 
          continue obtainbound; 
        } 
        if (contr) { 
          skip = true; 
          continue obtainbound; 
        } 
        skip = false; 
      } 
      if (skip) { 
        skip = false; 
        // the solution tree is completed 
        for (i=1; i<=n; i++) 
          if (!work1[i]) { 
            valh = a[subcripta][i]; 
            vali = a[i][subcripta]; 
            for (j=1; j<=n; j++) 
              if (!work2[j]) { 
                valg = valh * b[subcriptb][j] + vali * b[j][subcriptb]; 
                if (!contr) valg = -valg; 
                aux19[i][j] += valg; 
              } 
          } 
        if (!contr) { 
          // cancel the last single assignment 
          for (i=1; i<=n; i++) 
            if (!work1[i]) { 
              for (j=1; j<=n; j++) 
                if (!work2[j] && aux20[i][j] == k) aux20[i][j] = -1; 
            } 
          partialobj -= aux19[subcripta][subcriptb]; 
          work1[subcripta] = false; 
          work2[subcriptb] = false; 
          k--; 
          npp = n - k; 
          if (aux6[k+1] + partialobj >= bestobjval) { 
            if (k == 0) { 
              return; 
            } 
            subcripta = aux2[k]; 
            subcriptb = aux3[k]; 
            skip = true; 
            continue obtainbound; 
          } 
          qapsubprog3(n,k,npp,aux15,aux4,aux5,contr,aux16,aux17,aux18); 
          continue iterate; 
        } 
        aux10[subcripta] = subcriptb; 
        k++; 
        aux2[k] = subcripta; 
        aux3[k] = subcriptb; 
        if (k == (n-2)) { 
          // compute the objective function values 
          for (i=1; i<=n; i++) 
            if (!work1[i]) { 
              valx = i; 
              break; 
            } 
          for (i=1; i<=n; i++) 
            if (!work2[i]) { 
              j = i; 
              break; 
            } 
          work1[valx] = true; 
          work2[j] = true; 
          for (i=1; i<=n; i++) 
            if (!work1[i]) { 
              vale = i; 
              break; 
            } 
          for (i=1; i<=n; i++) 
            if (!work2[i]) { 
              valp = i; 
              break; 
            } 
          contr = false; 
          valw = 0; 
          while (true) { 
            partialz = aux19[valx][j] + aux19[vale][valp] +  
               a[valx][vale] * b[j][valp] + a[vale][valx] * b[valp][j]; 
            work1[valx] = false; 
            work2[j] = false; 
            if ((partialz + partialobj) < bestobjval) { 
              bestobjval = partialz + partialobj; 
              sol[0] = bestobjval; 
              for (i=1; i<=n; i++) 
                if (work1[i]) sol[i] = aux10[i]; 
              sol[valx] = j; 
              sol[vale] = valp; 
            } 
            if (valw != 0) { 
              if (k == 0) { 
                return; 
              } 
              subcripta = aux2[k]; 
              subcriptb = aux3[k]; 
              skip = true; 
              continue obtainbound; 
            } 
            valw = valx; 
            valx = vale; 
            vale = valw; 
          } 
        } 
        valf = 0; 
      } 
      if (valf < 1) { 
        // compute the alternative costs 
        qapsubprog1(npp,aux15,aux12,large,parm1,parm2,parm3); 
        subcripta = parm1[0]; 
        subcriptb = parm2[0]; 
        valk = parm3[0]; 
      } 
      else { 
        if (valf == 1) { 
          // compute the next single assignment 
          leastw = large; 
          valp = aux12[subcriptc]; 
          valc = (subcriptc - 1) * npp; 
          for (j=1; j<=npp; j++) { 
            valc++; 
            valg = aux15[valc]; 
            if ((leastw > valg) && (j != valp)) leastw = valg; 
          } 
          leastx = leastw; 
          leastw = large; 
          valc = valp; 
          for (i=1; i<=npp; i++) { 
            valg = aux15[valc]; 
            valc += npp; 
            if ((leastw > valg) && (i != subcriptc)) leastw = valg; 
          } 
          leastx += leastw; 
          leastw = large; 
          valc = subcriptd; 
          for (i=1; i<=npp; i++) { 
            valg = aux15[valc]; 
            valc += npp; 
            if ((valg < leastw) && (subcriptd != aux12[i])) leastw = valg; 
          } 
          leasty = leastw; 
          i = 1; 
          while (i <= npp) { 
            if (aux12[i] == subcriptd) break; 
            i++; 
          } 
          vale = i; 
          valc = (vale - 1) * npp; 
          leastw = large; 
          for (j=1; j<=npp; j++) { 
            valc++; 
            valg = aux15[valc]; 
            if ((valg < leastw) && (j != subcriptd)) leastw = valg; 
          } 
          if ((leastw + leasty) >= leastx) { 
            subcripta = vale; 
            subcriptb = subcriptd; 
            valk = leastw + leasty; 
          } 
          else { 
            subcripta = subcriptc; 
            subcriptb = valp; 
            valk = leastx; 
          } 
        } 
        else { 
          // compute the next single assignment 
          if (valy != 0) { 
            subcripta = valy; 
            subcriptb = aux12[subcripta]; 
          } 
          else { 
            subcriptb = valq; 
            i = 1; 
            while (i <= npp) { 
              if (aux12[i] == subcriptb) break; 
              i++; 
            } 
            subcripta = i; 
          } 
          leastw = large; 
          valc = (subcripta - 1) * npp; 
          for (i=1; i<=npp; i++) { 
            valc++; 
            valg = aux15[valc]; 
            if ((valg < leastw) && (i != subcriptb)) leastw = valg; 
          } 
          valk = leastw; 
          leastw = large; 
          valc = subcriptb; 
          for (j=1; j<=npp; j++) { 
            valg = aux15[valc]; 
            valc += npp; 
            if ((valg < leastw) && (j != subcripta)) leastw = valg; 
          } 
          valk += leastw; 
        } 
      } 
      valx = 0; 
      aux6[k+1] = valk + partialz; 
      i = 1; 
      while (i <= n) { 
        if (!work1[i]) { 
          valx++; 
          if (subcripta == valx) break; 
        } 
        i++; 
      } 
      subcripta = i; 
      valx = 0; 
      j = 1; 
      while (j <= n) { 
        if (!work2[j]) { 
          valx++; 
          if (subcriptb == valx) break; 
        } 
        j++; 
      } 
      subcriptb = j; 
      aux20[subcripta][subcriptb] = k; 
      contr = true; 
      work1[subcripta] = true; 
      work2[subcriptb] = true; 
      npp = n - k - 1; 
      // compute the cost matrix 
      qapsubprog2(n,k,npp,subcripta,subcriptb,a,b,aux4,aux7, 
                  work1,work2,aux16,aux17,aux13); 
      qapsubprog3(n,k,npp,aux15,aux4,aux5,contr,aux16,aux17,aux18); 
      valx = 0; 
      for (i=1; i<=n; i++) 
        if (!work1[i]) { 
          valh = a[i][subcripta]; 
          vali = a[subcripta][i]; 
          vald = valx; 
          for (j=1; j<=n; j++) 
            if (!work2[j]) { 
              vald++; 
              aux15[vald] += aux19[i][j] + valh * b[j][subcriptb] +  
                                           vali * b[subcriptb][j]; 
            } 
          valx += npp; 
        } 
      partialobj += aux19[subcripta][subcriptb]; 
    } 
  } 
} 
 
 
static private void qapsubprog1(int n, int aux15[], int aux12[], 
               int large, int parm1[], int parm2[], int parm3[]) 
{ 
  /* this method is used internally by quadraticAssignment */ 
 
  // compute the alternative costs and obtain the assignment 
 
  int i,j,leastw,leastx,p,q,valc,valp; 
 
  parm3[0] = -1; 
  valc = 0; 
  for (i=1; i<=n; i++) { 
    j = aux12[i]; 
    valp = j - n; 
    leastw = large; 
    for (p=1; p<=n; p++) { 
      valp += n; 
      if (p != i) { 
        q = aux15[valp]; 
        if (q < leastw) leastw = q; 
      } 
    } 
    leastx = leastw; 
    leastw = large; 
    for (p=1; p<=n; p++) { 
      valc++; 
      if (p != j) { 
        q = aux15[valc]; 
        if (q < leastw) leastw = q; 
      } 
    } 
    leastw += leastx; 
    if (leastw > parm3[0]) { 
      parm1[0] = i; 
      parm2[0] = j; 
      parm3[0] = leastw; 
    } 
  } 
} 
 
 
static private void qapsubprog2(int n, int k, int npp, int subcripta, 
                     int subcriptb, int a[][], int b[][], int aux4[], 
                     int vekt[], boolean work1[], boolean work2[], 
                     int aux16[], int aux17[], int aux13[]) 
{ 
  /* this method is used internally by quadraticAssignment */ 
 
  // obtain rows of the matrix a in decreasing order 
  // and rows of matrix b in increasing order 
 
  int i,j,nppa,nppb,nppc,valg,valp,valq,valx; 
  boolean decide; 
 
  nppa = npp - 1; 
  if (npp == n) { 
    valp = 1; 
    for (i=1; i<=n; i++) { 
      valx = 0; 
      for (j=1; j<=n; j++) 
        if (j != i) { 
          valx++; 
          vekt[valx] = a[i][j]; 
        } 
      qapsubprog5(vekt,aux13,nppa); 
      for (j=1; j<=nppa; j++) { 
        nppb = npp - j; 
        aux16[valp] = vekt[nppb]; 
        valp++; 
      } 
    } 
    valp = 1; 
    for (i=1; i<=n; i++) { 
      valx = 0; 
      for (j=1; j<=n; j++) 
        if (j != i) { 
          valx++; 
          vekt[valx] = b[i][j]; 
        } 
      qapsubprog5(vekt,aux13,nppa); 
      for (j=1; j<=nppa; j++) { 
        aux17[valp] = vekt[j]; 
        valp++; 
      } 
    } 
    return; 
  } 
  nppc = aux4[k+1]; 
  valp = nppc; 
  valq = nppc - (npp + 1) * npp; 
  for (i=1; i<=n; i++) 
    if (work1[i]) { 
      if (i == subcripta) valq += npp; 
    } 
    else { 
      valg = a[i][subcripta]; 
      decide = true; 
      for (j=1; j<=npp; j++) { 
        valq++; 
        if ((aux16[valq] == valg) && decide) 
          decide = false; 
        else { 
          valp++; 
          aux16[valp] = aux16[valq]; 
        } 
      } 
    } 
  valp = nppc; 
  valq = nppc - (npp + 1) * npp; 
  for (i=1; i<=n; i++) { 
    if (work2[i]) { 
      if (i == subcriptb) valq += npp; 
    } 
    else { 
      valg = b[i][subcriptb]; 
      decide = true; 
      for (j=1; j<=npp; j++) { 
        valq++; 
        if ((aux17[valq] == valg) && decide) 
          decide = false; 
        else { 
          valp++; 
          aux17[valp] = aux17[valq]; 
        } 
      } 
    } 
  } 
  return; 
} 
 
 
static private void qapsubprog3 (int n, int k, int npp, int aux15[], 
                 int aux4[], int aux5[], boolean contr, int aux16[], 
                 int aux17[], int aux18[]) 
{ 
  /* this method is used internally by quadraticAssignment */ 
 
  // compute the cost matrix of the k-th linear assignment problem 
 
  int accum,i,j,idx,nppd,valg,valp,valq,valr,vals,valt,valx; 
 
  idx = 0; 
  if (npp == n) { 
    accum = 0; 
    valp = 0; 
    if (contr) { 
      valq = accum; 
      nppd = npp - 1; 
      for (i=1; i<=npp; i++) { 
        valt = accum; 
        for (j=1; j<=npp; j++) { 
          valp++; 
          idx++; 
          valg = 0; 
          for (valx=1; valx<=nppd; valx++) { 
            valr = valq + valx; 
            vals = valt + valx; 
            valg += aux16[valr] * aux17[vals]; 
          } 
          valt += nppd; 
          aux18[valp] = valg; 
          aux15[idx] = valg; 
        } 
        valq += nppd; 
      } 
      return; 
    } 
    for (i=1; i<=npp; i++) 
      for (j=1; j<=npp; j++) { 
        valp++; 
        idx++; 
        aux15[idx] = aux18[valp]; 
      } 
    return; 
  } 
  if (!contr) { 
    valp = aux5[k]; 
    for (i=1; i<=npp; i++) 
      for (j=1; j<=npp; j++) { 
        valp++; 
        idx++; 
        aux15[idx] = aux18[valp]; 
      } 
    return; 
  } 
  accum = aux4[k+1]; 
  valp = aux5[k+1]; 
  valq = accum; 
  nppd = npp - 1; 
  for (i=1; i<=npp; i++) { 
    valt = accum; 
    for (j=1; j<=npp; j++) { 
      valp++; 
      idx++; 
      valg = 0; 
      for (valx=1; valx<=nppd; valx++) { 
        valr = valq + valx; 
        vals = valt + valx; 
        valg += aux16[valr] * aux17[vals]; 
      } 
      valt += nppd; 
      aux18[valp] = valg; 
      aux15[idx] = valg; 
    } 
    valq += nppd; 
  } 
  return; 
} 
 
 
static private void qapsubprog4(int n, int large, int c[], int parm[], 
                int wk1[], int wk2[], int wk3[], int wk4[], int wk5[], 
                int wk6[], int wk7[], boolean wk8[]) 
{ 
  /* this method is used internally by quadraticAssignment */ 
 
  // solve linear sum assignment problem 
 
  int i,j,p,v1,v2,v5,v6,v7,v8,v9,v10,v11,v12,v13,v14,v15; 
  int v3=0,v4=0,v16=0; 
 
  // initial assignment 
  for (i=1; i<=n; i++) { 
    wk1[i] = 0; 
    wk2[i] = 0; 
    wk5[i] = 0; 
    wk6[i] = 0; 
    wk7[i] = 0; 
  } 
  v1=0; 
  for (i=1; i<=n; i++) { 
    for (j=1; j<=n; j++) { 
      v1++; 
      v2 = c[v1]; 
      if (j == 1) { 
        v3 = v2; 
        v4 = j; 
      } 
      else { 
        if ((v2 - v3) < 0) { 
          v3 = v2; 
          v4 = j; 
        } 
      } 
    } 
    wk5[i] = v3; 
    if (wk1[v4] == 0) { 
      wk1[v4] = i; 
      wk2[i] = v4; 
    } 
  } 
  for (j=1; j<=n; j++) { 
    wk6[j] = 0; 
    if (wk1[j] == 0) wk6[j] = large; 
  } 
  v1 = 0; 
  for (i=1; i<=n; i++) { 
    v3 = wk5[i]; 
    for (j=1; j<=n; j++) { 
      v1++; 
      v15 = wk6[j]; 
      if (v15 > 0) { 
        v2 = c[v1] - v3; 
        if (v2 < v15) { 
          wk6[j] = v2; 
          wk7[j] = i; 
        } 
      } 
    } 
  } 
  for (j=1; j<=n; j++) { 
    i = wk7[j]; 
    if (i != 0) { 
      if (wk2[i] == 0) { 
        wk2[i] = j; 
        wk1[j] = i; 
      } 
    } 
  } 
  for (i=1; i<=n; i++) 
    if (wk2[i] == 0) { 
      v3 = wk5[i]; 
      v1 = (i - 1) * n; 
      for (j=1; j<=n; j++) { 
        v1++; 
        if (wk1[j] == 0) { 
          v2 = c[v1]; 
          if ((v2 - v3 - wk6[j]) <= 0) { 
            wk2[i] = j; 
            wk1[j] = i; 
            break; 
          } 
        } 
      } 
    } 
  // construct the optimal assignment 
  for (p=1; p<=n; p++) 
    if (wk2[p] <= 0) { 
      // compute shortest path 
      v5 = (p - 1) * n; 
      for (i=1; i<=n; i++) { 
        wk7[i] = p; 
        wk8[i] = false; 
        wk4[i] = large; 
        v6 = v5 + i; 
        wk3[i] = c[v6] - wk5[p] - wk6[i]; 
      } 
      wk4[p] = 0; 
      while (true) { 
        v14 = large; 
        for (i=1; i<=n; i++) 
          if (!wk8[i]) { 
            if (wk3[i] < v14) { 
              v14 = wk3[i]; 
              v16 = i; 
            } 
          } 
        if (wk1[v16] <= 0) break; 
        wk8[v16] = true; 
        v7 = wk1[v16]; 
        v8 = (v7 - 1) * n; 
        wk4[v7] = v14; 
        for (i=1; i<=n; i++) 
          if (!wk8[i]) { 
            v9 = v8 + i; 
            v10 = v14 + c[v9] - wk5[v7] - wk6[i]; 
            if (wk3[i] > v10) { 
              wk3[i] = v10; 
              wk7[i] = v7; 
            } 
          } 
      } 
      // augmentation 
      while (true) { 
        v7 = wk7[v16]; 
        wk1[v16] = v7; 
        v11 = wk2[v7]; 
        wk2[v7] = v16; 
        if (v7 == p) break; 
        v16 = v11; 
      } 
      // transformation 
      for (i=1; i<=n; i++) { 
        if (wk4[i] != large) 
          wk5[i] += v14 - wk4[i]; 
        if (wk3[i] < v14) 
          wk6[i] += wk3[i] - v14; 
      } 
    } 
  // compute the optimal value 
  parm[0] = 0; 
  for (i=1; i<=n; i++) { 
    v12 = (i - 1) * n; 
    j = wk2[i]; 
    v13 = v12 + j; 
    parm[0] += c[v13]; 
  } 
} 
 
 
static private void qapsubprog5 (int a[], int b[], int dim){ 
  /* this method is used internally by quadraticAssignment */ 
 
  // sort the vector a in increasing order 
  // b is the permutation vector of the sorted vector 
 
  int i,j,ina,inb,inx,iny,low,half,high,p,quant; 
 
  low = 1; 
  if (dim <= low) return; 
  half = (dim - low + 1) / 2; 
  quant = 1023; 
  for (p=1; p<=10; p++) { 
    if (quant <= half) { 
      high = dim - quant; 
      for (i=low; i<=high; i++) { 
        inx = i + quant; 
        ina = a[inx]; 
        inb = b[inx]; 
        j = i; 
        iny = inx; 
        while (ina < a[j]) { 
          a[iny] = a[j]; 
          b[iny] = b[j]; 
          iny = j; 
          j -= quant; 
          if (j < low) break; 
        } 
        a[iny] = ina; 
        b[iny] = inb; 
      } 
    } 
    quant /= 2; 
  } 
} 
 

}///