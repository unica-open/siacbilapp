<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<div id="divTabellaStanziamenti">
	<!--GESC014 TABELLA PER PROVA -->
	<h4>Stanziamenti</h4> 
	<s:include value="/jsp/cap/include/tabella_stanziamenti_con_componenti_spesa.jsp">
		<s:param name="visualizzaImportoIniziale">true</s:param>
	</s:include> 	
	<%-- <table  class="table  table-bordered" id="idStanziamentiTabellaPrev">
		<thead>
			<tr class="row-slim-bottom">
				<th>&nbsp;</th>
				<th>&nbsp;</th>
				<th scope="col" colspan="2" class="text-center">${annoEsercizioInt - 1}</th>
				<th scope="col" colspan="2" class="text-center">Residui ${annoEsercizioInt}</th>
				<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 0}</th>
				<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 1}</th>
				<th scope="col" colspan="2" class="text-center">${annoEsercizioInt + 2}</th>
				<th scope="col" colspan="2" class="text-center"> > ${annoEsercizioInt + 2}</th>
			</tr>
			<tr class="row-slim-top">
					<th>&nbsp;</th>
					<th>&nbsp;</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
				<th scope="col" class="text-center">Iniziale</th>
				<th scope="col" class="text-center">Finale</th>
			</tr>
		</thead>
		
		<tr  class="componentiRowFirst">
			<th rowspan = "3" class="stanziamenti-titoli"><a id="competenzaTotalePrev" href="#idStanziamentiTabellaPrev"  class="disabled" >Competenza</a></th>
			<td class="text-center">Stanziamento</td>
			<td class="text-right"><s:property value="importiEx.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnnoPrec.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right"><s:property value="importiResidui.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioResidui.importo" 		   /></td>
			<!-- -->
			<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione0.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno0.importo" /></td>									
			<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione1.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno1.importo" /></td>
			
			<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione2.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnno2.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right"><s:property value="importiAnniSuccessivi.stanziamentoIniziale" /></td>
			<td class="text-right"><s:property value="competenzaStanziamento.dettaglioAnniSucc.importo"/></td>
			<!-- -->
		</tr>
	
	
		<tr class="componentiRowOther">
			<td class="text-center">Impegnato</td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioResidui.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno0.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno1.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnno2.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="competenzaImpegnato.dettaglioAnniSucc.importo" /></td>
		</tr>
		<!--SIAC-7349 - Start - MR - 16/04/2020 Adeguamento, aggiunta riga disponibilita impegno dopo confronto con Analista P.G. -->
		<tr class="componentiRowOther">
			<td class="text-center">Disponibilit&agrave; ad impegnare</td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioResidui.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioAnno0.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioAnno1.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioAnno2.importo" /></td>
			<td class="text-right"></td>
			<td class="text-right"><s:property value="disponibilita.dettaglioAnniSucc.importo" /></td>
		</tr>
		<!--SIAC-7349 END-->	
		<tbody id="componentiCompetenzaPrev">
	
				<s:iterator value="importiComponentiCapitolo" var="cc" status="componentiCapitolo">
					
					
					
					
					<s:if test="#cc.tipoDettaglioComponenteImportiCapitolo.descrizione=='Stanziamento'">
						<tr  class="componentiCompetenzaRow previsione-default-stanziamento" >
					</s:if>
					<s:else>
						<tr  class="componentiCompetenzaRow" >
					</s:else>												
						
						<td id="description-component" class="componentiCompetenzaRow previsione-default-stanziamento" rowspan="1">
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
						</td><!--qui vanno inseriti i residui iniziali-->
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
			<tr class="componentiRowFirst">
				<th rowspan="2" class="stanziamenti-titoli">Residuo</th>
				<td class="text-center">Presunti</td>	
				<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoResiduoIniziale" /></td>
				<td class="text-right" scope="row"><s:property value="residuiPresunti.dettaglioAnnoPrec.importo" /></td>
				<!--SIAC 6881-->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaPrevisione0.stanziamentoResiduoIniziale" />
					</td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioResidui.importo" /></td>
				<!---->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaPrevisione2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno0.importo" /></td>
				
				<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione2.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnno2.importo" /></td>
				<!--SIAC -6881-->
				<td class="text-right"><s:property value="importiAnniSuccessivi.stanziamentoResiduoIniziale" /></td>
				<td class="text-right"><s:property value="residuiPresunti.dettaglioAnniSucc.importo" /></td>
				<!---->
			</tr>
			<tr class="componentiRowOther">
				<td class="text-center">Effettivi</td>
				<td class="text-right" scope="row"><s:property value="importiEx.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnnoPrec.importo" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioResiduiIniziale.importo" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioResidui.importo" /></td>
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaPrevisione0.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno0.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione1.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="importiCapitoloUscitaPrevisione2.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnno2.importo" /></td>
				<td class="text-right"><s:property value="importiAnniSuccessivi.residuoEffettivoIniziale" /></td>
				<td class="text-right"><s:property value="residuiEffettivi.dettaglioAnniSucc.importo" /></td>
				</tr>
			<tr class="componentiRowFirst">
				<th rowspan="2" class="stanziamenti-titoli">Cassa</th>
				<td class="text-center">Stanziamento</td>
	
				<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoCassaIniziale" /></td>
				<td class="text-right" scope="row"><s:property value="cassaStanziato.dettaglioAnnoPrec.importo" /></td>
				<!--SIAC 6881-->
				<td class="text-right"><s:property value="importiResidui.stanziamentoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioResidui.importo" /></td>
				<!---->
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaPrevisione0.stanziamentoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno0.importo" /></td>
				
				<td class="text-right" scope="row"><s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
				<td class="text-right"><s:property value="cassaStanziato.dettaglioAnno1.importo" /></td>
				<td class="text-right" scope="row"></td>
				<td class="text-right"></td>
				<!-- SIAC 6881-->
				<td class="text-right"></td>
				<td class="text-right"></td>
				<!---->
			</tr>
			<tr class="componentiRowOther">
				<td class="text-center">Pagato</td>
				<td class="text-right" scope="row"><s:property value="importiEx.pagatoCassaIniziale" /></td><!-- da rivedere dato -->
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnnoPrec.importo" /></td>
				<td class="text-right"><s:property value="importiResidui.pagatoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioResidui.importo" /></td>
				<td class="text-right" scope="row"><s:property value="cassaPagato.dettaglioResidui.importo" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnno0.importo" /></td>
				<td class="text-right" scope="row"><s:property value="importiCapitoloUscitaPrevisione1.pagatoCassaIniziale" /></td>
				<td class="text-right"><s:property value="cassaPagato.dettaglioAnno1.importo" /></td>
				<td class="text-right" scope="row"></td>
				<td class="text-right"></td>
				<td class="text-right"></td>
				<td class="text-right"></td>
			</tr>									
	</table> --%>
</div>
								