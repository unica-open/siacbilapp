<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<h4>Stanziamenti</h4>
<br/>

<table class="table table-hover table-bordered">  
	<thead>  
		<tr class="row-slim-bottom">  
			<th>&nbsp;</th>  
	<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
	<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
	<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
		</tr>  
		<tr class="row-slim-top">  
			<th>&nbsp;</th>  
			<th scope="col" class="text-center">Iniziale</th>  
			<th scope="col" class="text-center">Finale</th>  
			<th scope="col" class="text-center">Iniziale</th>  
			<th scope="col" class="text-center">Finale</th>  
			<th scope="col" class="text-center">Iniziale</th>  
			<th scope="col" class="text-center">Finale</th>  
		</tr>  
	</thead>  
	<tbody>  
		<tr>  
			<th>Competenza</th>  
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamento" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamento" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamento" /></td>
		</tr> 	
		<tr>  
			<th>- al netto impegni reimp. e da anni preced.</th>  
			<td class="text-right"></td>  
			<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td>
			<td class="text-right"></td>  
			<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td>
			<td class="text-right"></td>  
			<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td>
		</tr>  
		<s:if test="hasMassimoImpegnabile">
			<tr>
				<th>- assegnato</th>  
				<td class="text-right"></td>  
				<td class="text-right"><s:property value="massimoImpegnabile0" /></td>
				<td class="text-right"></td>  
				<td class="text-right"><s:property value="massimoImpegnabile1" /></td>
				<td class="text-right"></td>  
				<td class="text-right"><s:property value="massimoImpegnabile2" /></td>
			</tr>  
		</s:if>
<%-- 
 										<tr>  
 											<th>Residuo</th>  
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduo" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduo" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduo" /></td>
 										</tr>  
 										<tr>  
 											<th>Cassa</th>  
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassa" /></td>
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassa" /></td>
										<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassaIniziale" /></td>
										<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassa" /></td>
 										</tr>   --%>
	</tbody>  
</table> 
<br/> 
<s:include value="/jsp/cap/include/tabella_stanziamenti_con_componenti_spesa.jsp">
	<s:param name="visualizzaImportoIniziale">true</s:param>
</s:include> 	

<!-- 								<table class="table table-hover table-bordered"> -->
<!-- 									<thead> -->
<!-- 										<tr class="row-slim-bottom"> -->
<!-- 											<th>&nbsp;</th> -->
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th> --%>
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th> --%>
<%-- 											<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th> --%>
<!-- 										</tr> -->
<!-- 										<tr class="row-slim-top"> -->
<!-- 											<th>&nbsp;</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 											<th scope="col" class="text-center">Iniziale</th> -->
<!-- 											<th scope="col" class="text-center">Finale</th> -->
<!-- 										</tr> -->
<!-- 									</thead> -->
<!-- 									<tbody> -->
<!-- 										<tr> -->
<!-- 											<th>Competenza</th> -->
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamento" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamento" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamento" /></td> --%>
<!-- 										</tr> -->
		
<!-- 										<tr> -->
<!-- 											<th>- al netto impegni reimp. e da anni preced.</th> -->
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td> --%>
<!-- 											<td class="text-right"></td> -->
<%-- 											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td> --%>
<!-- 										</tr> -->
<%-- 										<s:if test="hasMassimoImpegnabile"> --%>
<!-- 											<tr> -->
<!-- 												<th>- assegnato</th> -->
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile0" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile1" /></td> --%>
<!-- 												<td class="text-right"></td> -->
<%-- 												<td class="text-right"><s:property value="massimoImpegnabile2" /></td> --%>
<!-- 											</tr> -->
<%-- 										</s:if> --%>

<!-- 										<tr> -->
<!-- 											<th>Residuo</th> -->
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduo" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduo" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduo" /></td> --%>
<!-- 										</tr> -->
<!-- 										<tr> -->
<!-- 											<th>Cassa</th> -->
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassa" /></td> --%>
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoCassa" /></td> --%>
<%-- 											<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassaIniziale" /></td> --%>
<%-- 											<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoCassa" /></td> --%>
<!-- 										</tr> -->
<!-- 									</tbody> -->
<!-- 								</table> -->

<!-- SIAC-6881 -->
<%-- <table class="table table-bordered" id="idStanziamentiTabella">
	<thead>
		<tr class="row-slim-bottom">
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt -1}</th>
			<th scope="col" colspan="2" class="text-center">Residui ${annoEsercizioInt + 0}</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
			<th scope="col" colspan="2" class="text-center">>${annoEsercizioInt + 2}</th>
		</tr>
		<tr class="row-slim-top">
			<th width="14%">&nbsp;</th>
			<th width="14%">&nbsp;</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
			<th scope="col" class="text-center" width="6%">Iniziale</th>
			<th scope="col" class="text-center" width="6%">Finale</th>
		</tr>
	</thead>
		<tr  class="componentiRowFirst">
			<th rowspan = "2"><a id="competenzaTotale" href="#idStanziamentiTabella"  class="disabled" >Competenza</a></th>
			<td class="text-center">Stanziamento</td>
			<td class="text-right"><s:property value="importiEsercizioPrecedente.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiResidui.stanziamentoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="competenzaStanziamento.dettaglioResidui.importo" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno0.importo" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione1.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno1.importo" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione2.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno2.importo" /></td>
			<td class="text-right"><s:property value="importiCapitoloSuccessivi.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnniSucc.importo" /></td>
		</tr>
		<tr class="componentiRowOther">
			<td class="text-center">Impegnato</td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right">SIAC-8089 <s:property value="competenzaImpegnato.dettaglioResidui.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.impegnato" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno1.impegnato" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno2.impegnato" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnniSucc.importo"  /></td>
		</tr>
												
		
		<tbody id="componentiCompetenza">

			<s:iterator value="importiComponentiCapitolo" var="cc" status="componentiCapitolo">
				
				<s:if test="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'">
					<tr  class="componentiCompetenzaRow previsione-default-stanziamento" >
				</s:if>
				<s:else>
				<tr  class="componentiCompetenzaRow" >
				</s:else>												
					
					<td id="description-component" class="componenti-competenza" rowspan="1">
						<s:property value="#cc.tipoComponenteImportiCapitolo.descrizione"/>
					</td> 
						
					<td class="text-center" id="type-component">
						<s:property value="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione"/>
					</td>
					<td class="text-right"></td>
					 <td class="text-right"> 
						<s:property value="#cc.dettaglioAnnoPrec.importo"/>
					</td>
					<td class="text-right">
						<s:property value="#cc.dettaglioResiduiIniziale.importo"/>
					</td>
					<td class="text-right">
						<s:property value="#cc.dettaglioResidui.importo"/>
					</td>
					<td class="text-right"></td>
					<td class="text-right">
						<s:property value="#cc.dettaglioAnno0.importo"/>
					</td>
					<td class="text-right"></td>
					<td class="text-right">
						<s:property value="#cc.dettaglioAnno1.importo"/>
					</td>
					<td class="text-right"></td>
					<td class="text-right">
						<s:property value="#cc.dettaglioAnno2.importo"/>
					</td>
					<td class="text-right"></td>
					<td class="text-right">
						<s:property value="#cc.dettaglioAnniSucc.importo"/>
					</td>
				</tr>
			</s:iterator>
		</tbody>
		
		
		
<!-- 										<tr> -->
<!-- 											<th>- al netto impegni reimp. e da anni preced.</th> -->
<!-- 											<td class="text-right"></td> -->
											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti0" /></td>
<!-- 											<td class="text-right"></td> -->
											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti1" /></td>
<!-- 											<td class="text-right"></td> -->
											<td class="text-right"><s:property value="nettoReimpEAnniPrecedenti2" /></td>
<!-- 										</tr> -->
										<s:if test="hasMassimoImpegnabile">
<!-- 											<tr> -->
<!-- 												<th>- assegnato</th> -->
<!-- 												<td class="text-right"></td> -->
												<td class="text-right"><s:property value="massimoImpegnabile0" /></td>
<!-- 												<td class="text-right"></td> -->
												<td class="text-right"><s:property value="massimoImpegnabile1" /></td>
<!-- 												<td class="text-right"></td> -->
												<td class="text-right"><s:property value="massimoImpegnabile2" /></td>
<!-- 											</tr> -->
										</s:if>

		<tr class="componentiRowFirst">
			<th rowspan="2">Residuo</th>
			<td class="text-center">Presunti</td>
			<td class="text-right"><s:property value="importiEsercizioPrecedente.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"><s:property value="importiCapitoloUscitaGestione0.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresunti.dettaglioResidui.importo" /></td>
			<td class="text-right" scope="row">SIAC-8089 <s:property value="importiResidui.stanziamentoResiduoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiPresunti.dettaglioAnno0.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloUscitaGestione1.stanziamentoResiduoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiPresunti.dettaglioAnno1.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloUscitaGestione2.stanziamentoResiduoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiPresunti.dettaglioAnno2.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloSuccessivi.stanziamentoResiduoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiPresunti.dettaglioAnniSucc.importo" /></td>
		</tr>
		<tr class="componentiRowOther">
			<td class="text-center">Effettivi</td>
			<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnnoPrec.importo"  /></td>
			<td class="text-right"><s:property value="residuiEffettivi.dettaglioResiduiIniziale.importo" /></td>
			<td class="text-right"><s:property value="residuiEffettivi.dettaglioResidui.importo" /></td>
			<td class="text-right" scope="row">SIAC-8089 <s:property value="importiCapitoloUscitaGestione0.residuoEffettivoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiEffettivi.dettaglioAnno0.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloUscitaGestione1.residuoEffettivoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiEffettivi.dettaglioAnno1.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloUscitaGestione2.residuoEffettivoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiEffettivi.dettaglioAnno2.importo" /></td>
			<td class="text-right">SIAC-8089 <s:property value="importiCapitoloSuccessivi.residuoEffettivoIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="residuiEffettivi.dettaglioAnniSucc.importo" /></td>
		</tr>
		<tr class="componentiRowFirst">
			<th rowspan="2">Cassa</th>
			<td class="text-center">Stanziamento</td>
			<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziato.dettaglioAnnoPrec.importo"  /></td>
			<td class="text-right"><s:property value="importiResidui.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziato.dettaglioResidui.importo"  /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno0.importo" /></td>
			<td class="text-right" scope="row">SIAC-8089 <s:property value="importiCapitoloUscitaGestione1.stanziamentoCassaIniziale" /></td>
			<td class="text-right">SIAC-8089 <s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
			<td class="text-right" scope="row"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
		</tr>
		<tr class="componentiRowOther">
			<td class="text-center">Pagato</td>
			<td class="text-right" scope="row"><s:property value="importiEsercizioPrecedente.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaPagato.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"><s:property value="importiResidui.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneResiduo.pagato" /></td>
			<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaGestione0.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="disponibilitaCapitoloUscitaGestioneAnno0.pagato" /></td>
			<td class="text-right" scope="row">SIAC-8089 <s:property value="importiCapitoloUscitaGestione1.pagatoCassaIniziale" /></td>
			<td class="text-right">
			SIAC-8089 
				<s:if test="%{disponibilitaCapitoloUscitaGestioneAnno1.pagato==null}">
					0,00
				</s:if>
				<s:else>
					<s:property value="disponibilitaCapitoloUscitaGestioneAnno1.pagato" />
				</s:else> 
			
			</td>
			<td class="text-right" scope="row"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
		</tr>
</table> --%>

								