<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s" %>
<h4>Stanziamenti</h4>
<table class="table table-bordered">
	<thead>
		<tr class="row-slim-bottom">
			<th>&nbsp;</th>
			<th>&nbsp;</th>
			<th scope="col" colspan="2" class="text-center">${annoEsercizioInt - 1}</th>
			<th scope="col" colspan="2" class="text-center">Residui ${annoEsercizioInt + 0}</th>
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
	<tr>
		<!--Nel caso ci fosse anche la disponibilita a impegnare, settare il rowspan a 3 e decommentare la tr corrispondente-->
		<th rowspan = "2"><a id="competenzaTotaleConsGest" href="#idStanziamentiTabellaConsGest"  class="disabled" >Competenza</a></th>
		<td class="text-center">Stanziamento</td>
		<td class="text-right"><s:property value="importiEx.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioAnnoPrec.importo" /></td>
		<!--SIAC 6881-->
		<td class="text-right"><s:property value="importiResiduiEquiv.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioResidui.importo" /></td>
		<!-- -->
		<td class="text-right"><s:property value="importiEx1.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioAnno0.importo" /></td>									
		<td class="text-right"><s:property value="importiEx2.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioAnno1.importo" /></td>
		<td class="text-right"><s:property value="importiEx3.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioAnno2.importo" /></td>
		<!--SIAC 6881-->
		<td class="text-right"><s:property value="importiAnniSuccessiviEquiv.stanziamentoIniziale" /></td>
		<td class="text-right"><s:property value="competenzaStanziamentoEq.dettaglioAnniSucc.importo" /></td>
		<!-- -->
	</tr>
	<tr>
		<td class="text-center">Impegnato</td>
		<td class="text-right"></td>
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioAnnoPrec.importo" /></td> <!--da rivedere-->
		<td class="text-right"></td>
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioResidui.importo" /></td>
		<td class="text-right"></td> <!-- al momento scolpite -->
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioAnno0.importo" /></td>
		<td class="text-right"></td>
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioAnno1.importo" /></td>
		<td class="text-right"></td>
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioAnno2.importo" /></td>
		<td class="text-right"></td>
		<td class="text-right"><s:property value="competenzaImpegnatoEq.dettaglioAnniSucc.importo" /></td>
	</tr>
	
	<tbody id="componentiCompetenzaConsGest">

			<s:iterator value="importiComponentiCapitoloEquiv" var="cc" status="componentiCapitolo">
				
				<tr>												
					
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
					<td class="text-right"></td>
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
	<tr>
			<th rowspan="2">Residuo</th>
			<td class="text-center">Presunti</td>	
			<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoResiduoIniziale" /></td>
			<td class="text-right" scope="row"><s:property value="residuiPresuntiEq.dettaglioAnnoPrec.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right" scope="row"><s:property value="importiResiduiEquiv.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresuntiEq.dettaglioResidui.importo" /></td>
			<!---->
			<td class="text-right" scope="row"><s:property value="importiEx1.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresuntiEq.dettaglioAnno0.importo" /></td>
			
			<td class="text-right"><s:property value="importiEx2.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresuntiEq.dettaglioAnno1.importo" /></td>
			<td class="text-right"><s:property value="importiEx3.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresuntiEq.dettaglioAnno2.importo" /></td>
			<!--SIAC -6881-->
			<td class="text-right"><s:property value="importiAnniSuccessiviEquiv.stanziamentoResiduoIniziale" /></td>
			<td class="text-right"><s:property value="residuiPresuntiEq.dettaglioAnniSucc.importo" /></td>
			<!---->
		</tr>
		<tr><!-- discuterne con i residui effettivi e presunti-->
			<td class="text-center">Effettivi</td>
			<td class="text-right" scope="row"><s:property value="importiEx.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"><s:property value="importiResiduiEquiv.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioResidui.importo" /></td>
			<td class="text-right" scope="row"><s:property value="importiEx1.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioAnno0.importo" /></td>
			<td class="text-right"><s:property value="importiEx2.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioAnno1.importo" /></td>
			<td class="text-right"><s:property value="importiEx3.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioAnno2.importo" /></td>
			<td class="text-right"><s:property value="importiAnniSuccessiviEquiv.residuoEffettivoIniziale" /></td>
			<td class="text-right"><s:property value="residuiEffettiviEq.dettaglioAnniSucc.importo" /></td>
			</tr>
		<tr>
			<th rowspan="2">Cassa</th>
			<td class="text-center">Stanziamento</td>

			<td class="text-right" scope="row"><s:property value="importiEx.stanziamentoCassaIniziale" /></td>
			<td class="text-right" scope="row"><s:property value="cassaStanziatoEq.dettaglioAnnoPrec.importo" /></td>
			<!--SIAC 6881-->
			<td class="text-right"><s:property value="importiResiduiEquiv.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziatoEq.dettaglioResidui.importo" /></td>
			<!---->
			<td class="text-right"><s:property value="importiEx1.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziatoEq.dettaglioAnno0.importo" /></td>
			
			<td class="text-right" scope="row"><s:property value="importiEx2.stanziamentoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaStanziatoEq.dettaglioAnno1.importo" /></td>
			<td class="text-right" scope="row"></td>
			<td class="text-right"></td>
			<!-- SIAC 6881-->
			<td class="text-right"></td>
			<td class="text-right"></td>
			<!---->
		</tr>
		<tr>
			<td class="text-center">Pagato</td>
			<td class="text-right" scope="row"><s:property value="importiEx.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaPagatoEq.dettaglioAnnoPrec.importo" /></td>
			<td class="text-right"><s:property value="importiResiduiEquiv.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaPagatoEq.dettaglioResidui.importo" /></td>
			<td class="text-right" scope="row"><s:property value="importiEx1.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaPagatoEq.dettaglioAnno0.importo" /></td>
			<td class="text-right" scope="row"><s:property value="importiEx2.pagatoCassaIniziale" /></td>
			<td class="text-right"><s:property value="cassaPagatoEq.dettaglioAnno1.importo" /></td>
			<td class="text-right" scope="row"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
			<td class="text-right"></td>
		</tr>
		<%--<tr>
				<th>
				<abbr title="fondo pluriennale vincolato">FPV</abbr>
				</th>
				<td>&nbsp;</td>
				<td><s:property value="capitolo.importiCapitoloEquivalente.fondoPluriennaleVinc" /></td>
			</tr>--%>
</table>
<h4>Variazioni</h4>
<s:hidden name="capitolo.uidCapitoloEquivalente" id="HIDDEN_UID_EQUIVALENTE" />
<table class="table table-hover table-bordered" id="tabellaMovimentiGestione" data-overlay>
	<thead>
		<tr>
			<th> </th>
			<th> </th>
			<th class="text-center" colspan="4" abbr="anno" scope="col">${annoEsercizioInt + 0}</th>
		</tr>
		<tr>
			<th> </th>
			<th> </th>
			<th class="text-center" scope="col">
			<abbr title="numero">Num</abbr>
			</th>
			<th class="text-center" scope="col">
			<abbr title="importo">Competenza</abbr>
			</th>
			<th class="text-center" scope="col">
			<abbr title="importo">Residuo</abbr>
			</th>
			<th class="text-center" scope="col">
			<abbr title="importo">Cassa</abbr>
			</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<th scope="rowgroup" rowspan="3">Variazioni in aumento</th>
			<th scope="rowgroup">Definitive</th>
			<td scope="row" data-property="variazioniInAumentoDefinitivaNum0" data-numero></td>
			<td data-property="variazioniInAumentoDefinitiveCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoDefinitiveResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoDefinitiveCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Giunta o Consiglio</th>
			<td scope="row" data-property="variazioniInAumentoGiuntaConsiglioNum0" data-numero></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoGiuntaConsiglioCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Totali</th>
			<td scope="row" data-property="variazioniInAumentoTotaleNum0" data-numero></td>
			<td data-property="variazioniInAumentoTotaleCompetenza0" data-importo></td>
			<td data-property="variazioniInAumentoTotaleResiduo0" data-importo></td>
			<td data-property="variazioniInAumentoTotaleCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup" rowspan="3">Variazioni in diminuzione</th>
			<th scope="rowgroup">Definitive</th>
			<td scope="row" data-property="variazioniInDiminuzioneDefinitivaNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneDefinitiveCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneDefinitiveResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneDefinitiveCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Giunta o Consiglio</th>
			<td scope="row" data-property="variazioniInDiminuzioneGiuntaConsiglioNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneGiuntaConsiglioCassa0" data-importo></td>
		</tr>
		<tr>
			<th scope="rowgroup">Totali</th>
			<td scope="row" data-property="variazioniInDiminuzioneTotaleNum0" data-numero></td>
			<td data-property="variazioniInDiminuzioneTotaleCompetenza0" data-importo></td>
			<td data-property="variazioniInDiminuzioneTotaleResiduo0" data-importo></td>
			<td data-property="variazioniInDiminuzioneTotaleCassa0" data-importo></td>
		</tr>
	</tbody>
</table>