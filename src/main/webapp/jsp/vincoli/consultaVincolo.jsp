<%--
SPDX-FileCopyrightText: Copyright 2020 | CSI Piemonte
SPDX-License-Identifier: EUPL-1.2
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
			<div class="span12 ">
				<div class="contentPage">
					<s:include value="/jsp/include/messaggi.jsp" />
					<h3>Consulta Vincolo: <s:property value="vincolo.codice"/></h3>
					
					<fieldset class="form-horizontal">
						<dl class="dl-horizontal">
							<dt>Descrizione</dt>
							<dd><s:property value="vincolo.descrizione"/>&nbsp;</dd>
							<dt>Trasferimenti vincolati</dt>
							<dd>
								<s:if test="%{vincolo.flagTrasferimentiVincolati}">
									S&iacute;
								</s:if><s:else>
									No
								</s:else>
							</dd>
							<dt>Note</dt>
							<dd><s:property value="vincolo.note"/>&nbsp;</dd>
							<dt>Stato</dt>
							<dd><s:property value="stato"/>&nbsp;</dd>
							<dt>Bilancio</dt>
							<dd><s:property value="tipoBilancio"/>&nbsp;</dd>
							<dt>Tipo</dt>
							<dd><s:property value="genereVincolo"/>&nbsp;</dd>
							<!-- SIAC-7192 -->
							<dt>Risorsa vincolata</dt>
							<dd><s:property value="vincolo.risorsaVincolata.descrizione"/>&nbsp;</dd>
						</dl>
					</fieldset>
					
					<h4>Capitoli di entrata</h4>
					<table class="table table-hover dataTable" id="tabellaCapitoliEntrata" summary="...." >
						<thead>
							<tr>
								<th scope="col">Capitolo</th>
								<th scope="col">Classificazione</th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt}"/></th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 1}"/></th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 2}"/></th>
								<th scope="col">Strutt. Amm. Resp.</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="listaCapitoliEntrata" var="entry">
								<tr>
									<td>
										<a href="#" data-trigger="hover" data-toggle="popover"  data-content="<s:property value="%{#entry.descrizione}"/>" data-original-title="Descrizione">
											<s:property value="%{#entry.capitolo}"/>
										</a>
									</td>
									<td><s:property value="%{#entry.classificazione}"/></td>
									<td><s:property value="%{#entry.competenzaAnno0}"/></td>
									<td><s:property value="%{#entry.competenzaAnno1}"/></td>
									<td><s:property value="%{#entry.competenzaAnno2}"/></td>
									<td><s:property value="%{#entry.strutturaAmministrativoContabile}"/></td>
								</tr>
							</s:iterator>
						</tbody>
						<tfoot>
							<tr>
								<th scope="col">Totale</th>
								<th scope="col">&nbsp;</th>
								<th scope="col"><s:property value="totaleStanziamentoEntrata0"/></th>
								<th scope="col"><s:property value="totaleStanziamentoEntrata1"/></th>
								<th scope="col"><s:property value="totaleStanziamentoEntrata2"/></th>
								<th scope="col">&nbsp;</th>
							</tr>
						</tfoot>
					</table>
					
					<h4>Capitoli di spesa</h4>
					<table class="table table-hover dataTable" id="tabellaCapitoliSpesa" summary="...." >
						<thead>
							<tr>
								<th scope="col">Capitolo</th>
								<th scope="col">Classificazione</th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt}"/></th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 1}"/></th>
								<th scope="col">Stanz. comp. <s:property value="%{annoEsercizioInt + 2}"/></th>
								<th scope="col">Strutt. Amm. Resp.</th>
							</tr>
						</thead>
						<tbody>
							<s:iterator value="listaCapitoliUscita" var="entry">
								<tr>
									<td>
										<a href="#" data-trigger="hover" data-toggle="popover"  data-content="<s:property value="%{#entry.descrizione}"/>" data-original-title="Descrizione">
											<s:property value="%{#entry.capitolo}"/>
										</a>
									</td>
									<td><s:property value="%{#entry.classificazione}"/></td>
									<td><s:property value="%{#entry.competenzaAnno0}"/></td>
									<td><s:property value="%{#entry.competenzaAnno1}"/></td>
									<td><s:property value="%{#entry.competenzaAnno2}"/></td>
									<td><s:property value="%{#entry.strutturaAmministrativoContabile}"/></td>
								</tr>
							</s:iterator>
						</tbody>
						<tfoot>
							<tr>
								<th scope="col">Totale</th>
								<th scope="col">&nbsp;</th>
								<th scope="col"><s:property value="totaleStanziamentoUscita0"/></th>
								<th scope="col"><s:property value="totaleStanziamentoUscita1"/></th>
								<th scope="col"><s:property value="totaleStanziamentoUscita2"/></th>
								<th scope="col">&nbsp;</th>
							</tr>
						</tfoot>
					</table>
					
					<p>
						<s:include value="/jsp/include/indietro.jsp" />
					</p>
				</div>
			</div>
		</div>
	</div>

	<s:include value="/jsp/include/footer.jsp" />
	
	<s:include value="/jsp/include/javascript.jsp" />
	<script type="text/javascript" src="/siacbilapp/js/local/vincolo/consulta.js"></script>

</body>
</html>