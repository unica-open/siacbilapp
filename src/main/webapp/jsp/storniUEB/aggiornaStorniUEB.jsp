<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ taglib uri="http://www.csi.it/taglibs/remincl-1.0" prefix="r"%>

<%-- Inclusione head e CSS --%>
<s:include value="/jsp/include/head.jsp" />
</head>
<body>
	<%-- Inclusione header --%>
	<s:include value="/jsp/include/header.jsp" />

	<div class="container-fluid">
		<div class="row-fluid">
			<div class="span12 contentPage">
				<s:form cssClass="form-horizontal" action="aggiornamentoStornoUEB" novalidate="novalidate">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Aggiorna storni UEB</h3>

					<div class="well">
						<dl class="dl-horizontal">
							<dt>Numero storno:</dt>
							<dd>&nbsp;<s:property value="numeroStorno"/></dd>
							<dt>UEB Sorgente:</dt>
							<dd>&nbsp;<s:property value="tipoCapitoloSorgente"/>&nbsp;-&nbsp;<s:property value="numeroCapitoloSorgente"/>&nbsp;/&nbsp;<s:property value="numeroArticoloSorgente"/>&nbsp;/&nbsp;<s:property value="numeroUEBSorgente"/></dd>
							<dt>Disponibilit&agrave; Sorgente:</dt>
							<dd>&nbsp;<s:property value="disponibilitaCapitoloSorgente"/></dd>
							<dt>UEB Destinazione:</dt>
							<dd>&nbsp;<s:property value="tipoCapitoloDestinazione"/>&nbsp;-&nbsp;<s:property value="numeroCapitoloDestinazione"/>&nbsp;/&nbsp;<s:property value="numeroArticoloDestinazione"/>&nbsp;/&nbsp;<s:property value="numeroUEBDestinazione"/></dd>
							<dt>Disponibilit&agrave; Destinazione:</dt>
							<dd>&nbsp;<s:property value="disponibilitaCapitoloDestinazione"/></dd>
							<dt>Provvedimento:</dt>
							<dd>&nbsp;<s:property value="annoProvvedimento"/>&nbsp;/&nbsp;<s:property value="numeroProvvedimento"/>
						</dl>
						<%-- Campi hidden di utilità --%>
						<s:hidden name="uidCapitoloSorgente" data-maintain="" />
						<s:hidden name="uidCapitoloDestinazione" data-maintain="" />
						<s:hidden name="numeroStorno" data-maintain="" />
						<s:hidden name="uidStorno" data-maintain="" />
						<s:hidden name="statoOperativoVariazioneDiBilancio" data-maintain="" />
						<s:hidden name="tipoCapitoloSorgente" data-maintain="" />
						<s:hidden name="numeroCapitoloSorgente" data-maintain="" />
						<s:hidden name="numeroArticoloSorgente" data-maintain="" />
						<s:hidden name="numeroUEBSorgente" data-maintain="" />
						<s:hidden name="disponibilitaCapitoloSorgente" data-maintain="" />
						<s:hidden name="tipoCapitoloDestinazione" data-maintain="" />
						<s:hidden name="numeroCapitoloDestinazione" data-maintain="" />
						<s:hidden name="numeroArticoloDestinazione" data-maintain="" />
						<s:hidden name="numeroUEBDestinazione" data-maintain="" />
						<s:hidden name="disponibilitaCapitoloDestinazione" data-maintain="" />
					</div>
					<table summary="riepilogo incarichi" class="table table-hover">

						<tr>
							<th>&nbsp;</th>
							<th><s:property value="%{annoEsercizioInt}"/> </th>
							<th><s:property value="%{annoEsercizioInt + 1}"/></th>
							<th><s:property value="%{annoEsercizioInt + 2}"/></th>
						</tr>
						<tr>
							<th>Competenza</th>
							<td>
								<s:textfield id="competenzaStorno0" cssClass="input-small soloNumeri decimale-negativo" name="competenzaStorno0" />
								<label for="competenzaStorno0" class="nascosto">inserisci importo</label>
							</td>
							<td>
								<s:textfield id="competenzaStorno1" cssClass="input-small soloNumeri decimale-negativo" name="competenzaStorno1" />
								<label for="competenzaStorno1" class="nascosto">inserisci importo</label>
							</td>
							<td>
								<s:textfield id="competenzaStorno2" cssClass="input-small soloNumeri decimale-negativo" name="competenzaStorno2" />
								<label for="competenzaStorno2" class="nascosto">inserisci importo</label>
							</td>
						</tr>
						<tr>
							<th>Cassa</th>
							<td>
								<s:textfield id="cassaStorno0" cssClass="input-small soloNumeri decimale-negativo" name="cassaStorno0" />
								<label for="imp4" class="nascosto">inserisci importo</label>
							</td>
							<td>&nbsp;</td>
							<td>&nbsp;</td>
						</tr>
					</table>
					<div class="collapse-group">
						<div class="margin-large">
							<a class="btn pull-center" data-toggle="collapse" data-parent="#accordion4" href="#collapseProvvedimento">
								<i class="icon-pencil icon-2x"></i><span id="SPAN_InformazioniProvvedimento"><s:property value="stringaProvvedimento" /></span>
							</a>
						</div>
						<div id="collapseProvvedimento" class="collapse">
							<div class="collapse-inner">
								<p>&Egrave; necessario inserire oltre all'anno almeno il numero atto oppure il tipo atto</p>
								<%-- Ricerca come dato aggiuntivo --%>
								<s:include value="/jsp/provvedimento/formRicercaProvvedimento.jsp" />
							</div>
						</div>
					</div>

					<p class="margin-large">
						<s:include value="/jsp/include/indietro.jsp" />
						<button type="button" class="btn btn-link reset">annulla</button>
						<s:submit cssClass="btn btn-primary pull-right" value="salva"/>
					</p>
				</s:form>
			</div>
		</div>
	</div>
	
	<%-- Caricamento del footer --%>
	<s:include value="/jsp/include/footer.jsp" />
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/storniUEB/storni.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ricerca_collapse.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/provvedimento/ztree.js"></script>
	<script type="text/javascript" src="/siacbilapp/js/local/storniUEB/aggiorna.js"></script>

</body>
</html>