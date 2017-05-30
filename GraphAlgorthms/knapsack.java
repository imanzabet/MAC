package GraphAlgorithms;

public class knapsack{
	
public knapsack(){
}
public static void multipleKnapsack(int n, int m, int profit[], int weight[], 
                                    int capacity[], int depth, int sol[]) 
{ 
  int i, i1, p, idx, idx1, idx2, j, y, tmp, netp, slackbnd, res1a, ubtmp; 
  int minweight, totalweight, depthtmp, n1, m1, proftmp, ubslack; 
  int q=0, upperbnd=0, indexj=0, indexi=0; 
  float r; 
  float pwratio[] = new float[n + 1]; 
  int res1[] = new int[1]; 
  int res2[] = new int[1]; 
  int res3[] = new int[1]; 
  int res4[] = new int[1]; 
  int totalprofit[] = new int[1]; 
  int origp[] = new int[n + 1]; 
  int origw[] = new int[n + 1]; 
  int origindex[] = new int[n + 1]; 
  int aux0[] = new int[m + 1]; 
  int aux1[] = new int[m + 1]; 
  int aux2[] = new int[m + 1]; 
  int aux3[] = new int[m + 1]; 
  int aux4[] = new int[n + 1]; 
  int aux5[] = new int[n + 1]; 
  int aux6[] = new int[n + 1]; 
  int aux7[] = new int[n + 1]; 
  int aux8[] = new int[n + 1]; 
  int aux9[] = new int[n + 2]; 
  int aux10[] = new int[n + 2]; 
  int aux11[] = new int[n + 2]; 
  int aux12[][] = new int[m+1][n + 1]; 
  int aux13[][] = new int[m+1][n+1]; 
  int aux14[][] = new int[m+1][n+1]; 
  int aux15[][] = new int[m+1][n+2]; 
  boolean control[] = new boolean[1]; 
  boolean skip=false, outer=false; 
  boolean unmark[] = new boolean[n + 1]; 
 
  // check for invalid input data 
  totalprofit[0] = 0; 
  if (n <= 1) totalprofit[0] = - 1; 
  if (m <= 0) totalprofit[0] = - 1; 
  if (totalprofit[0] < 0) { 
    sol[0] = totalprofit[0];  
    return; 
  } 
  minweight = weight[1]; 
  totalweight = weight[1]; 
  if (profit[1] <= 0) totalprofit[0] = -2; 
  if (weight[1] <= 0) totalprofit[0] = -2; 
  for (j=2; j<=n; j++) { 
    if (profit[j] <= 0) totalprofit[0] = -2; 
    if (weight[j] <= 0) totalprofit[0] = -2; 
    if (weight[j] < minweight) minweight = weight[j]; 
    totalweight += weight[j]; 
  } 
 
  // store the original input 
  for (j=1; j<=n; j++) { 
    origp[j] = profit[j]; 
    origw[j] = weight[j]; 
    pwratio[j] = ((float) profit[j]) / ((float) weight[j]); 
    unmark[j] = true; 
  } 
      
  // sort the input 
  for (i=1; i<=n; i++) { 
    r = -1.0F; 
    for (j=1; j<=n; j++) 
      if (unmark[j]) { 
        if (pwratio[j] > r) { 
          r = pwratio[j]; 
          q = j; 
        } 
      } 
    unmark[q] = false; 
    profit[i] = origp[q]; 
    weight[i] = origw[q]; 
    origindex[i] = q; 
  } 
 
  if (capacity[1] <= 0) totalprofit[0] = -2; 
  if (m == 1) { 
    // solve the special case of one knapsack problem 
    if (minweight > capacity[1]) totalprofit[0] = -3; 
    if (totalweight <= capacity[1]) { 
      // the knapsacks contain all the items 
      q = 0; 
      for (j=1; j<=n; j++) { 
        profit[j] = origp[j]; 
        weight[j] = origw[j]; 
        sol[j] = 1; 
        q += origw[j]; 
      } 
      sol[0] = q; 
      return; 
    } 
    if (totalprofit[0] < 0) { 
      sol[0] = totalprofit[0];  
       for (j=1; j<=n; j++) { 
        profit[j] = origp[j]; 
        weight[j] = origw[j]; 
      } 
     return; 
    } 
    res4[0] = capacity[1]; 
    for (j=1; j<=n; j++) { 
      aux10[j] = profit[j]; 
      aux11[j] = weight[j]; 
    } 
    // compute the solution with one knapsack 
    mkpsSingleKnapsack(n,n,res4,0,totalprofit,aux8,aux10,aux11); 
    depth = 0; 
    sol[0] = totalprofit[0]; 
    for (j=1; j<=n; j++) { 
      profit[j] = origp[j]; 
      weight[j] = origw[j]; 
      sol[origindex[j]] = aux8[j]; 
    } 
    return; 
  } 
  for (i=2; i<=m; i++) { 
    if (capacity[i] <= 0) totalprofit[0] = -2; 
    if (capacity[i] < capacity[i-1]) { 
      sol[0] = -4; 
      for (j=1; j<=n; j++) { 
        profit[j] = origp[j]; 
        weight[j] = origw[j]; 
      } 
      return; 
    } 
  } 
  if (minweight > capacity[1]) totalprofit[0] = -3; 
  if (totalweight <= capacity[m]) totalprofit[0] = -4; 
  if (totalprofit[0] < 0) { 
    sol[0] = totalprofit[0];  
    for (j=1; j<=n; j++) { 
      profit[j] = origp[j]; 
      weight[j] = origw[j]; 
    } 
    return; 
  } 
 
  // initialize 
  depthtmp = depth; 
  depth = 0; 
  netp = 0; 
  n1 = n + 1; 
  aux9[n1] = 1; 
  m1 = m - 1; 
  for (j=1; j<=n; j++) { 
    aux9[j] = 1; 
    for (i=1; i<=m; i++) { 
      aux13[i][j] = 0; 
      aux12[i][j] = 0; 
    } 
  } 
  for (i=1; i<=m1; i++) { 
    aux2[i] = capacity[i]; 
    aux0[i] = -1; 
  } 
  aux2[m] = capacity[m]; 
  totalprofit[0] = 0; 
  slackbnd = 0; 
  idx = 1; 
  // compute an upper bound of the current solution 
  mkpsCurrentUpperBound(n,m,profit,weight,capacity,1,netp,res1,res3, 
        aux5,aux7,aux8,aux9,aux10,aux11); 
  for (j=1; j<=n; j++) 
    aux6[j] = aux5[j]; 
  res1a = res1[0]; 
  ubtmp = res3[0]; 
  control[0] = false; 
 
  while (true) { 
    // using heuristic approximation 
    outer = false; 
    netp = totalprofit[0] - slackbnd; 
    // get a feasible solution 
    mkpsFeasibleSolution(n,m,profit,weight,idx,netp,res2,aux1,aux2, 
                 aux3,aux7,aux8,aux9,aux10,aux11,aux12,aux14,aux15); 
    if (res2[0] + slackbnd > totalprofit[0]) { 
      totalprofit[0] = res2[0] + slackbnd; 
      for (j=1; j<=n; j++) { 
        sol[j] = 0; 
        for (y=1; y<=idx; y++) 
          if (aux13[y][j] != 0) { 
            sol[j] = y; 
            break; 
          } 
      } 
      idx1 = aux1[i]; 
      if (idx1 != 0) { 
        for (j=1; j<=idx1; j++) { 
          q = aux15[idx][j]; 
          if (aux14[idx][j] == 1) sol[q] = idx; 
        } 
      } 
      i1 = idx + 1; 
      for (p=i1; p<=m; p++) { 
        idx1 = aux1[p]; 
        if (idx1 != 0) 
          for (j=1; j<=idx1; j++) { 
            q = aux15[p][j]; 
            if (aux14[p][j] == 1) sol[q] = p; 
          } 
      } 
      if (res3[0] == res2[0]) { 
        outer = true; 
      } 
    } 
    if (!outer) { 
      skip = false; 
      while (true) { 
        if (aux3[idx] != 0) { 
          // update 
          ubslack = res3[0] + slackbnd; 
          tmp = aux1[idx]; 
          proftmp = 0; 
          for (y=1; y<=tmp; y++) { 
            if (aux14[idx][y] != 0) { 
              j = aux15[idx][y]; 
              aux13[idx][j] = 1; 
              aux2[idx] -= weight[j]; 
              slackbnd += profit[j]; 
              aux9[j] = 0; 
              aux12[idx][j] = aux0[idx]; 
              aux4[j] = ubslack; 
              if (!control[0]) { 
                upperbnd = ubslack; 
                indexj = j; 
                indexi = idx; 
              } 
              aux0[idx] = j; 
              proftmp += profit[j]; 
              if (proftmp == aux3[idx]) { 
                skip = true; 
                break; 
              } 
              // upper bound computation 
              mkpsCalculateBound(idx,idx,res3,control,slackbnd,upperbnd, 
                                 indexj,indexi,n,res1,res1a,ubtmp,aux0, 
                                 aux2,aux5,aux6,aux9,aux12); 
              if (!control[0]) { 
                netp = totalprofit[0] - slackbnd; 
                // compute an upper bound of the current solution 
                mkpsCurrentUpperBound(n,m,profit,weight,aux2,idx,netp, 
                            res1,res3,aux5,aux7,aux8,aux9,aux10,aux11); 
                indexj = n1; 
              } 
              ubslack = res3[0] + slackbnd; 
              if (ubslack <= totalprofit[0]) { 
                outer = true; 
                break; 
              } 
            } 
          } 
          if (skip || outer) break; 
        } 
        if (idx == m - 1) { 
          outer = true; 
          break; 
        } 
        idx2 = idx + 1; 
        // upper bound computation 
        mkpsCalculateBound(idx2,idx,res3,control,slackbnd,upperbnd,indexj, 
                indexi,n,res1,res1a,ubtmp,aux0,aux2,aux5,aux6,aux9,aux12); 
        if (!control[0]) { 
          netp = totalprofit[0] - slackbnd; 
          // compute an upper bound of the current solution 
          mkpsCurrentUpperBound(n,m,profit,weight,aux2,idx2,netp,res1, 
                                res3,aux5,aux7,aux8,aux9,aux10,aux11); 
          indexj = n1; 
        } 
        if (res3[0] + slackbnd <= totalprofit[0]) { 
          outer = true; 
          break; 
        } 
        idx++; 
      } 
    } 
    while (true) { 
      // backtrack 
      if (idx <= 0) { 
        depth--; 
        sol[0] = totalprofit[0]; 
        for (j=1; j<=n; j++) 
          aux8[j] = sol[j]; 
        for (j=1; j<=n; j++) { 
          profit[j] = origp[j]; 
          weight[j] = origw[j]; 
          sol[origindex[j]] = aux8[j]; 
        } 
        return; 
      } 
      if (depth == depthtmp) { 
        sol[0] = totalprofit[0]; 
        for (j=1; j<=n; j++) 
          aux8[j] = sol[j]; 
        for (j=1; j<=n; j++) { 
          profit[j] = origp[j]; 
          weight[j] = origw[j]; 
          sol[origindex[j]] = aux8[j]; 
        } 
        return; 
      } 
      depth++; 
      if (aux0[idx] == -1) { 
        for (j=1; j<=n; j++) 
          aux12[idx][j] = 0; 
        idx--; 
        continue; 
      } 
      j = aux0[idx]; 
      aux13[idx][j] = 0; 
      aux9[j] = 1; 
      slackbnd -= profit[j]; 
      aux2[idx] += weight[j]; 
      for (y=1; y<=n; y++) 
        if (aux12[idx][y] == j) aux12[idx][y] = 0; 
      aux0[idx] = aux12[idx][j]; 
      if (aux4[j] > totalprofit[0]) break; 
    } 
    res3[0] = aux4[j] - slackbnd; 
    control[0] = true; 
  } 
} 
 
 
static private void mkpsCurrentUpperBound(int n, int m, int profit[], 
       int weight[], int aux2[], int i, int netp, int res1[],  
       int res3[], int aux5[], int aux7[], int aux8[], int aux9[], 
       int aux10[], int aux11[]) 
{ 
  /* this method is used internally by multipleKnapsack */ 
 
  // Compute an upper bound of the current solution 
 
  int j, q, wk1, wk2; 
  int ref1[] = new int[1]; 
 
  wk1 = 0; 
  ref1[0] = 0; 
  for (j=i; j<=m; j++) 
    ref1[0] += aux2[j]; 
  wk2 = 0; 
  for (j=1; j<=n; j++) { 
    aux5[j] = 0; 
    if (aux9[j] != 0) { 
      wk1++; 
      aux7[wk1] = j; 
      aux10[wk1] = profit[j]; 
      aux11[wk1] = weight[j]; 
      wk2 += weight[j]; 
    } 
  } 
  if (wk2 <= ref1[0]) { 
    res1[0] = ref1[0] - wk2; 
    res3[0] = 0; 
    if (wk1 == 0) return; 
    for (j=1; j<=wk1; j++) { 
      res3[0] += aux10[j]; 
      aux8[j] = 1; 
    } 
  } 
  else { 
    // compute the solution with one knapsack 
    mkpsSingleKnapsack(n,wk1,ref1,netp,res3,aux8,aux10,aux11); 
    res1[0] = ref1[0]; 
  } 
  for (j=1; j<=wk1; j++) { 
    q = aux7[j]; 
    aux5[q] = aux8[j]; 
  } 
} 
 
     
static private void mkpsFeasibleSolution(int n, int m, int profit[], 
       int weight[], int i, int netp, int res2[], int aux1[], int aux2[], 
       int aux3[], int aux7[], int aux8[], int aux9[], int aux10[], 
       int aux11[], int aux12[][], int aux14[][], int aux15[][]) 
{ 
  /* this method is used internally by multipleKnapsack */ 
 
  // Get a feasible solution 
 
  int p,j,q,netpa,accu1,accu2,accu3,accu4,accu5; 
  int ref1[] = new int[1]; 
  int pb[] = new int[1]; 
 
  accu5 = 0; 
  for (j=1; j<=n; j++) 
    if (aux9[j] != 0) { 
      accu5++; 
      aux7[accu5] = j; 
    } 
  for (j=i; j<=m; j++) { 
    aux1[j] = 0; 
    aux3[j] = 0; 
  } 
  res2[0] = 0; 
  netpa = netp; 
  if (accu5 == 0) return; 
  accu3 = 0; 
  accu4 = 0; 
  for (j=1; j<=accu5; j++) { 
    q = aux7[j]; 
    if (aux12[i][q] == 0) { 
      if (weight[q] <= aux2[i]) { 
        accu3++; 
        accu4 += weight[q]; 
        aux15[i][accu3] = q; 
        aux10[accu3] = profit[q]; 
        aux11[accu3] = weight[q]; 
      } 
    } 
  } 
  p = i; 
  while (true) { 
    aux1[p] = accu3; 
    if (accu4 <= aux2[p]) { 
      pb[0] = 0; 
      if (accu3 != 0) { 
        for (j=1; j<=accu3; j++) { 
          pb[0] += aux10[j]; 
          aux14[p][j] = 1; 
        } 
      } 
    } 
    else { 
      ref1[0] = aux2[p]; 
      netp = 0; 
      if (p == m) netp = netpa; 
      // compute the solution with one knapsack 
      mkpsSingleKnapsack(n,accu3,ref1,netp,pb,aux8,aux10,aux11); 
      for (j=1; j<=accu3; j++) 
        aux14[p][j] = aux8[j]; 
    } 
    res2[0] += pb[0]; 
    netpa -= pb[0]; 
    aux3[p] = pb[0]; 
    aux15[p][accu3+1] = n + 1; 
    if (p == m) return; 
    accu1 = 1; 
    accu2 = 0; 
    for (j=1; j<=accu5; j++) { 
      if (aux7[j] >= aux15[p][accu1]) { 
        accu1++; 
        if (aux14[p][accu1-1] == 1) continue; 
      } 
      accu2++; 
      aux7[accu2] = aux7[j]; 
    } 
    accu5 = accu2; 
    if (accu5 == 0) return; 
    accu3 = 0; 
    accu4 = 0; 
    p++; 
    for (j=1; j<=accu5; j++) { 
      q = aux7[j]; 
      if(weight[q] <= aux2[p]) { 
        accu3++; 
        accu4 += weight[q]; 
        aux15[p][accu3] = q; 
        aux10[accu3] = profit[q]; 
        aux11[accu3] = weight[q]; 
      } 
    } 
  } 
} 
 
 
static private void mkpsCalculateBound(int i, int p, int res3[], 
       boolean control[], int slackbnd, int upperbnd, int indexj, 
       int indexi, int n, int res1[], int res1a, int ubtmp, 
       int aux0[], int aux2[], int aux5[], int aux6[], int aux9[], 
       int aux12[][]) 
{ 
  /* this method is used internally by multipleKnapsack */ 
 
  // Upper bound computation 
 
  int j,id1,id2,id3,id4; 
    
  control[0] = false; 
  if (aux9[indexj] == 0) { 
    id1 = i - 1; 
    if (id1 >= indexi) { 
      id2 = 0; 
      for (j=indexi; j<=id1; j++) 
        id2 += aux2[j]; 
      if (id2 > res1[0]) return; 
    } 
    id3 = p; 
    id4 = aux0[id3]; 
    while (true) { 
      if (id4 == -1) { 
        id3--; 
        id4 = aux0[id3]; 
      } 
      else { 
        if (aux5[id4] == 0) return; 
        if (id4 == indexj) break; 
        id4 = aux12[id3][id4]; 
      } 
    } 
    res3[0] = upperbnd - slackbnd; 
    control[0] = true; 
    return; 
  } 
  id1 = i - 1; 
  if (id1 >= 1) { 
    id2 = 0; 
    for (j=1; j<=id1; j++) 
      id2 += aux2[j]; 
    if (id2 > res1a) return; 
  } 
  for (j=1; j<=n; j++) 
    if (aux9[j] != 1) 
      if (aux6[j] == 0) return; 
  res3[0] = ubtmp - slackbnd; 
  control[0] = true; 
} 
 
 
static private void mkpsSingleKnapsack(int n, int ns, int ref1[], 
       int netp,int ref2[], int aux8[], int aux10[], int aux11[]) 
{ 
  /* this method is used internally by multipleKnapsack */ 
 
  // Compute the solution with one knapsack 
 
  int p,p1,in,j,j1,q,diff,index2,index3,index4,index5,index6; 
  int thres,thres1,val2,prev,n1,r; 
  int val1=0,index1=0,index7=0,t=0; 
  float tmp1,tmp2,tmp3; 
  int work1[] = new int[n + 1]; 
  int work2[] = new int[n + 1]; 
  int work3[] = new int[n + 1]; 
  int work4[] = new int[n + 1]; 
  int work5[] = new int[n + 1]; 
  boolean skip=false, jump=false, middle=false, over=false, outer=false; 
 
  ref2[0] = netp; 
  index3 = 0; 
  index2 = ref1[0]; 
  for (j=1; j<=ns; j++) { 
    index1 = j; 
    if (aux11[j] > index2) break; 
    index3 += aux10[j]; 
    index2 -= aux11[j]; 
  } 
  index1--; 
  if (index2 != 0) { 
    aux10[ns+1] = 0; 
    aux11[ns+1] = ref1[0] + 1; 
    thres = index3 + index2 * aux10[index1+2] / aux11[index1+2]; 
    tmp1 = index3 + aux10[index1+1]; 
    tmp2 = (aux11[index1+1] - index2) * aux10[index1]; 
    tmp3 = aux11[index1]; 
    thres1 = (int) (tmp1 - tmp2 / tmp3); 
    if (thres1 > thres) thres = thres1; 
    if (thres <= ref2[0]) return; 
    val2 = ref1[0] + 1; 
    work2[ns] = val2; 
    for (j=2; j<=ns; j++) { 
      index7 = ns + 2 - j; 
      if (aux11[index7] < val2) val2 = aux11[index7]; 
      work2[index7-1] = val2; 
    } 
    for (j=1; j<=ns; j++) 
      work1[j] = 0; 
    index5 = 0; 
    prev = ns; 
    p = 1; 
    skip = true; 
  } 
  else { 
    if (ref2[0] >= index3) return; 
    ref2[0] = index3; 
    for (j=1; j<=index1; j++) 
      aux8[j] = 1; 
    index6 = index1 + 1; 
    for (j=index6; j<=ns; j++) 
      aux8[j] = 0; 
    ref1[0] = 0; 
    return; 
  } 
  middle = false; 
  while (true) { 
    if (!skip) { 
      if (aux11[p] > ref1[0]) { 
        p1 = p + 1; 
        if (ref2[0] >= ref1[0] * aux10[p1] / aux11[p1] + index5) { 
          middle = true; 
        } 
        if (!middle) { 
          p = p1; 
          continue; 
        } 
      } 
      if (!middle) { 
        index3 = work3[p]; 
        index2 = ref1[0] - work4[p]; 
        in = work5[p]; 
        index1 = ns; 
        if (in <= ns) { 
          for (j=in; j<=ns; j++) { 
            index1 = j; 
            if (aux11[j] > index2) { 
              index1--; 
              if (index2 == 0) break; 
              if (ref2[0] >= index5 + index3 + index2 * aux10[j] / aux11[j]) { 
                middle = true; 
                break; 
              } 
              skip = true;         
              break; 
            } 
            index3 += aux10[j]; 
            index2 -= aux11[j]; 
          } 
        } 
      } 
      if (!middle) { 
        if (!skip) { 
          if (ref2[0] >= index3 + index5) { 
            middle = true; 
          } 
          if (!middle) { 
            ref2[0] = index3 + index5; 
            val1 = index2; 
            index6 = p - 1; 
            for (j=1; j<=index6; j++) 
              aux8[j] = work1[j]; 
            for (j=p; j<=index1; j++) 
              aux8[j] = 1; 
            if (index1 != ns) { 
              index6 = index1 + 1; 
              for (j=index6; j<=ns; j++) 
                aux8[j] = 0; 
            } 
            if (ref2[0] != thres) { 
              middle = true; 
            } 
            if (!middle) { 
              ref1[0] = val1; 
              return; 
            } 
          } 
        } 
      } 
    } 
    if (!middle) { 
      skip = false; 
      work4[p] = ref1[0] - index2; 
      work3[p] = index3; 
      work5[p] = index1 + 1; 
      work1[p] = 1; 
      index6 = index1 - 1; 
      if (index6 >= p) 
        for (j=p; j<=index6; j++) { 
          work4[j+1] = work4[j] - aux11[j]; 
          work3[j+1] = work3[j] - aux10[j]; 
          work5[j+1] = index1 + 1; 
          work1[j+1] = 1; 
        } 
      j1 = index1 + 1; 
      for (j=j1; j<=prev; j++) { 
        work4[j] = 0; 
        work3[j] = 0; 
        work5[j] = j; 
      } 
      prev = index1; 
      ref1[0] = index2; 
      index5 += index3; 
      if ((index1 - (ns - 2)) > 0)  
        p = ns; 
      else if ((index1 - (ns - 2)) == 0) { 
        if (ref1[0] >= aux11[ns]) { 
          ref1[0] -= aux11[ns]; 
          index5 += aux10[ns]; 
          work1[ns] = 1; 
        } 
        p = ns - 1; 
      } 
      else { 
        p = index1 + 2; 
        if (ref1[0] >= work2[p-1]) continue; 
      } 
      if (ref2[0] < index5) { 
        ref2[0] = index5; 
        for (j=1; j<=ns; j++) 
          aux8[j] = work1[j]; 
        val1 = ref1[0]; 
        if (ref2[0] == thres) return; 
      } 
      if (work1[ns] != 0) { 
        work1[ns] = 0; 
        ref1[0] += aux11[ns]; 
        index5 -= aux10[ns]; 
      } 
    } 
    outer = false; 
    while (true) { 
      middle = false; 
      index6 = p - 1; 
      jump = false; 
      if (index6 != 0) { 
        for (j=1; j<=index6; j++) { 
          index7 = p - j; 
          if (work1[index7] == 1) { 
            jump = true; 
            break; 
          } 
        } 
      } 
      if (!jump) { 
        ref1[0] = val1; 
        return; 
      } 
      r = ref1[0]; 
      ref1[0] += aux11[index7]; 
      index5 -= aux10[index7]; 
      work1[index7] = 0; 
      if (r >= work2[index7]) { 
        p = index7 + 1; 
        outer = true; 
        break; 
      } 
      index6 = index7 + 1; 
      p = index7; 
      over = false; 
      while (true) { 
        if (ref2[0] >= index5 + ref1[0] * aux10[index6] / aux11[index6]) { 
          over = true; 
          break; 
        } 
        diff = aux11[index6] - aux11[index7]; 
        if (diff < 0) { 
          t = r - diff; 
          if (t < work2[index6]) { 
            index6++; 
            continue; 
          } 
          break; 
        } 
        if (diff == 0) {  
          index6++; 
          continue; 
        } 
        if (diff > r) { 
          index6++; 
          continue; 
        } 
        if (ref2[0] >= index5 + aux10[index6]) { 
          index6++; 
          continue; 
        } 
        ref2[0] = index5 + aux10[index6]; 
        for (j=1; j<=index7; j++) 
          aux8[j] = work1[j]; 
        q = index7 + 1; 
        for (j=q; j<=ns; j++) 
          aux8[j] = 0; 
        aux8[index6] = 1; 
        val1 = ref1[0] - aux11[index6]; 
        if (ref2[0] == thres) { 
          ref1[0] = val1; 
          return; 
        } 
        r -= diff; 
        index7 = index6; 
        index6++; 
      } 
      if (!over) { 
        n = index6 + 1;  
        if (ref2[0] < index5 + aux10[index6] + t * aux10[n] / aux11[n]) 
          break; 
      } 
      else 
        over = false; 
    } 
    if (!outer) { 
      ref1[0] -= aux11[index6]; 
      index5 += aux10[index6]; 
      work1[index6] = 1; 
      p = index6 + 1; 
      work4[index6] = aux11[index6]; 
      work3[index6] = aux10[index6]; 
      work5[index6] = p; 
      n1 = index6 + 1; 
      for (j=n1; j<=prev; j++) { 
        work4[j] = 0; 
        work3[j] = 0; 
        work5[j] = j; 
      } 
      prev = index6; 
    } 
    else 
      outer = false; 
  } 
}

}
